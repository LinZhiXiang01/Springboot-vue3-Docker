<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  name:null,
  table:[

  ],
  pageNum:1,
  pageSize:10,
  total:0,
  formVisible:false,
  form:{},
  ids:[],
  rules:{
    username:[
      {required: true,message: '请输入账号', trigger: 'blur'}
    ],
    name:[
      {required: true,message: '请输入名字', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

const loadData = () =>{
  request.get("/employee/selectPage",
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
  formRef.value.validate((valid) => {
    if(valid){
      data.form.id ? update() : add()
    }
  })
}

const add=()=>{ // add的对象里面没有id
  request.post("/employee/add",data.form).then(res =>{
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
  request.put("/employee/update",data.form).then(res =>{
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
  ElMessageBox.confirm("确认删除该员工吗？",'删除确认', {type:"warning"}).then(()=>{
    request.delete("/employee/deleteById/"+id).then(res =>{
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
  ElMessageBox.confirm("确认删除该员工吗？",'删除确认', {type:"warning"}).then(()=>{
    request.delete("employee/deleteBatch",{data: data.ids}).then(res =>{
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
  console.log(res)
  data.form.avatar = res.data
}


loadData()
</script>

<template>
  <div>
    <div class = "card" style="margin-bottom: 5px">
      <el-input style="width: 240px; margin-right:20px" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="warning"@click="resetData">重置</el-button>
    </div>
    <div class = "card" style="margin-bottom: 5px">
      <el-button type="primary" @click = "handleAdd">新增</el-button>
      <el-button type="warning" @click = "delBatch">批量删除</el-button>
      <el-button type="info">导入</el-button>
      <el-button type="success">导出</el-button>

    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.table" stripe border @selection-change="handleSelectionChange">
        <el-table-column type="selection"  width="55" />
        <el-table-column label="账号" prop="username" />
        <el-table-column label="头像" >
          <template #default="scope">
            <img :src="scope.row.avatar" alt=""  style="display: block ;width: 40px; height: 40px; border-radius: 50%;">
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name" />
        <el-table-column label="性别" prop="sex" />
        <el-table-column label="工号" prop="no" />
        <el-table-column label="年龄" prop="age" />
        <el-table-column label="个人介绍" prop="description" show-overflow-tooltip />
        <el-table-column label="部门" prop="departmentName" />
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
  <el-dialog  v-model="data.formVisible" title="员工信息" width="500" destroy-on-close>
    <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="70px" style="padding-right:40px ;padding-left:10px" >
      <el-form-item label="账号" prop="username">
        <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
      </el-form-item>

      <el-form-item label="头像" prop="avatar">
      <el-upload
          action="http://localhost:9090/files/upload"
          :on-success="handleAvatarSuccess"
          list-type="picture"
      >
        <el-button type = "primary">上传头像</el-button>
      </el-upload>
      </el-form-item>

      <el-form-item label="名称" prop="name">
        <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="data.form.sex">
          <el-radio value="男" label="男"></el-radio>
          <el-radio value="女" label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="工号" label-width="70px">
        <el-input v-model="data.form.no" autocomplete="off" placeholder="请输入工号"/>
      </el-form-item>
      <el-form-item label="年龄" label-width="70px">
        <el-input-number v-model="data.form.age" style = "width:180px" min="18" autocomplete="off" placeholder="请输入年龄"/>
      </el-form-item>
      <el-form-item label="个人介绍" label-width="70px">
        <el-input rows=3 type="textarea" v-model="data.form.description" style = "width:400px"  autocomplete="off" placeholder="请输入个人介绍"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>