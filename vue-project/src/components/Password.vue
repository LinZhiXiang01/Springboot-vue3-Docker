<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== data.form.newPassword) {
    callback(new Error("Two inputs don't match!"))
  } else {
    callback()
  }
}

const formRef = ref();
const data = reactive({
  user:JSON.parse(localStorage.getItem('xm-pro-user')),
  form:{},
  rule: {
    password: [
      { required: true, message: '请输入原密码', trigger: 'blur' },
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
    ],
    confirmPassword:[
      { required: true,message:'请再次确认新密码',validator: validatePass, trigger: 'blur' }
    ]
  }

})


const updatePassword =()=>{
  data.form.id = data.user.id
  data.form.role = data.user.role
  formRef.value.validate(valid => {
    if (valid){
      request.put('updatePassword',data.form).then(res=>{
        if(res.code===200){
          ElMessage.success("修改成功")
          localStorage.removeItem('xm-pro-user')
          setTimeout(() => {
            location.href = '/login'
          },500)
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<template>

  <div class = "card" style="width: 500px;">
    <el-form ref = "formRef" :rules="data.rule" :model= "data.form" >
      <el-form-item label="原密码" prop="password">
        <el-input show-password  v-model="data.form.password" placeholder="请输入原密码"></el-input>
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input show-password  v-model="data.form.newPassword" placeholder="请输入新密码" prefix-icon="Lock"></el-input>
      </el-form-item>

      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input show-password  v-model="data.form.confirmPassword" placeholder="请再次确认新密码"></el-input>
      </el-form-item>
      <div>
        <el-button type="primary" @click="updatePassword">立即修改</el-button>
      </div>

    </el-form>
  </div>
</template>

<style scoped>

</style>