<script setup>

import {reactive,ref} from "vue";
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";


const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem("xm-pro-user")),
  form : {},
  rules: {
    username:[
        {required: true, message: "请输入账号", trigger: blur}
    ],
    name:[
        {required: true, message: "请输入名称", trigger: blur}
    ],
    no:[
        {required: true, message: "请输入工号", trigger: blur}
    ],
    age:[
        {required: true, message: "请输入年龄", trigger: blur}
    ],
    description:[
        {required: true, message: "请输入个人介绍", trigger: blur}
    ]

  }
})
const updatePersonalInfo = () => {
  formRef.value.validate((valid) => {
    if (valid){
      if(data.form.role === "ADMIN") { // 管理员
        request.put('admin/update', data.form).then(res => {
          if (res.code === 200) {
            ElMessage.success("更新成功")
            //更新缓存数据
            localStorage.setItem("xm-pro-user", JSON.stringify(data.form))
            // 触发父级从缓存里取的最新个人信息
            emit('updatePersonalInfo')
          } else {
            ElMessage.error(res.msg)
          }
        })
      }else{ // 员工
        data.form.authId = data.user.profile.authId
        request.put('employee/update', data.form).then(res => {
          if (res.code === 200) {
            ElMessage.success("更新成功")
            //更新缓存数据

            // 2. 更新 profile 的内容
            data.user.profile.name = data.form.name
            data.user.profile.avatar = data.form.avatar
            data.user.profile.sex = data.form.sex
            data.user.profile.no = data.form.no
            data.user.profile.age = data.form.age
            data.user.profile.description = data.form.description

            localStorage.setItem("xm-pro-user", JSON.stringify(data.user))
            // 触发父级从缓存里取的最新个人信息
            emit('updatePersonalInfo')
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    }
  })
}

const emit = defineEmits(['updatePersonalInfo'])

const handleAvatarSuccess = (res)=>{
  console.log(res)
  data.form.avatar = res.data
}


if(data.user.role ==="EMP"){
  request.get('employee/selectProfileById'+"/"+data.user.profile.authId).then(res =>{
    data.form = res.data
    data.form.role = "EMP"
  })
}else {
  data.form = data.user
}


</script>

<template>
  <div class = "card" style="padding-top:60px ;padding-left: 30px;padding-right: 30px;margin-bottom: 5px ;width: 50% ;  ">
  <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="70px" style="padding-right:40px ;padding-left:10px" >
      <!-- 头像-->
    <el-form-item label="头像">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:8080/files/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar"  alt=""/>
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
    </el-form-item>
    <el-form-item label="账号" prop="username">
      <el-input disabled v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
    </el-form-item>
    <el-form-item label="名称" prop="name">
      <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
    </el-form-item>
    <div v-if="data.form.role==='EMP'">
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
    </div>

    <div>
      <el-button type = primary @click="updatePersonalInfo" size = "large" style="width: 100%">
        更新个人信息
      </el-button>
    </div>
  </el-form>
  </div>
</template>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>