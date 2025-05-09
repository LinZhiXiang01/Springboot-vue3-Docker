<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount,shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const data = reactive({
  title:null,
  table:[

  ],
  pageNum:1,
  pageSize:10,
  total:0,
  formVisible:false,
  form:{},
  ids:[],

  viewVisible: false,
  content: null
})

/* wangEditor5 初始化开始 */
const baseUrl = 'http://localhost:8080'
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

const view = (content) => {
  data.content = content
  data.viewVisible = true
}

const loadData = () =>{
  request.get("/article/selectPage",
      {
        params:{
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          name: data.name
        }
      }).then(res =>{
        data.table = res.data.list
        data.total = res.data.total
        data.pageSize = res.data.pageSize
        data.pageNum = res.data.pageNum

      })
}

const resetData = () =>{
  data.name = null
  loadData()
}

const handleAdd =() =>{
  data.formVisible = true
  data.form = {}
}

const handleUpdate =(row) =>{
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true
}

const save =()=>{
  data.form.id ? update() : add()
}

const add=()=>{ // add的对象里面没有id
  request.post("/article/add",data.form).then(res =>{
    if(res.code===200)  {
      data.formVisible = false
      ElMessage.success("保存成功")
      loadData()
    }else{
      ElMessage.error("保存失败")
    }
  })
}

const update=()=>{ // 编辑的对象里面包含id
  request.put("/article/update",data.form).then(res =>{
    if(res.code===200)  {
      data.formVisible = false
      ElMessage.success("保存成功")
      loadData()
    }else{
      ElMessage.error("保存失败")
    }
  })
}

const del=(id)=>{
  ElMessageBox.confirm("确认删除文章吗？",'删除确认', {type:"warning"}).then(()=>{
    request.delete("/article/deleteById/"+id).then(res =>{
      if(res.code === 200)  {
        ElMessage.success("删除成功")
        loadData()
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const delBatch=()=>{
  if(data.ids.length === 0)  {
    ElMessage.warning("请选择要删除的记录")
    return
  }
  ElMessageBox.confirm("确认删除这些文章吗？",'删除确认', {type:"warning"}).then(()=>{
    request.delete("article/deleteBatch",{data: data.ids}).then(res =>{
      if(res.code === 200)  {
        ElMessage.success("删除成功")
        loadData()
      }else{
        ElMessage.error(res.msg)
      }
    }).catch()
  })
}

const handleSelectionChange = (rows)=>{
//从行数组里面，取出所有id，形成新的数组
  data.ids = rows.map(row=>row.id)
  console.log(data.ids)
}

const handleAvatarSuccess = (res)=>{
  data.form.img = res.data
}

const getImageUrl = async (originalUrl) => {
  try {
    // 通过axios获取图片数据（会触发拦截器）
    const response = await request.get(originalUrl, {
      responseType: 'blob' // 关键：指定响应类型为二进制
    });

    // 生成临时Blob URL
    return URL.createObjectURL(response.data);
  } catch (error) {
    console.error('图片加载失败', error);
    return ''; // 返回占位图或空
  }
};

loadData()
</script>

<template>
  <div>
    <div class = "card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right:20px" v-model="data.title" placeholder="请输入标题查询" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="warning" @click="resetData">重置</el-button>
    </div>
    <div class = "card" style="margin-bottom: 5px">
      <el-button type="primary" @click = "handleAdd">新增</el-button>
      <el-button type="warning" @click = "delBatch">批量删除</el-button>
      <el-button type="info">导入</el-button>
      <el-button type="success">导出</el-button>

    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.table" stripe border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="标题" prop="title" />
        <el-table-column label="封面" prop="name" >
          <template #default = "scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list=[scope.row.img] preview-teleported alt="" style="width: 100px; height: 100px"/>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" show-overflow-tooltip />
        <el-table-column label="内容" prop="content" >
          <template #default="scope">
            <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="time" />
        <el-table-column label="操作" prop="operations" >
          <template #default="scope">
            <el-button  type="primary" size="small" :icon="Edit" circle @click="handleUpdate(scope.row)"></el-button>
            <el-button  type="danger" size="small" :icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px">
        <el-pagination
            @size-change="loadData"
            @current-change="loadData"
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 15, 20]"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.total"
        />
      </div>
    </div>
  </div>

<!--  表单-->
  <el-dialog  v-model="data.formVisible" title="文章信息" width="500" destroy-on-close>
    <el-form :model="data.form" ref="formRef"  label-width="70px" style="padding-right:40px ;padding-left:10px" >
      <el-form-item label="标题" prop="title">
        <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
      </el-form-item>
      <el-form-item label="封面" prop="img">
        <el-upload
            action="/files/upload"
            :on-success="handleAvatarSuccess"
            list-type="picture"
        >
          <el-button type = "primary">上传封面</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="简介" prop="description">
        <el-input type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入简介"/>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <div style="border: 1px solid #ccc; width: 100%">
          <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :mode="mode"
          />
          <Editor
              style="height: 500px; overflow-y: hidden;"
              v-model="data.form.content"
              :mode="mode"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
    <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
    <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.viewVisible = false">关 闭</el-button>
    </span>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>