<script setup>
  import {reactive, ref} from "vue";
  import request from "@/utils/request.js"
  import {ElMessage, ElMessageBox} from "element-plus";

  const validatePass = (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请再次确认密码'))
    } else if (value !== data.form.password) {
      callback(new Error("Two inputs don't match!"))
    } else {
      callback()
    }
  }


  const data = reactive({
    form:{},
    rules:{
      username:[
        { required: true,message:'请输入账号',trigger:'blur'}
      ],
      password:[
        { required: true,message:'请输入密码',trigger:'blur'}
      ],
      confirmPassword:[
        { validator: validatePass, trigger: 'blur' }
      ]
    }
  })

  const formRef = ref()

  const register = () =>{
      formRef.value.validate(valid => {
        if (valid) {
          request.post("/register",data.form).then((res) => {
            if (res.code === 200){
              localStorage.setItem("xm-pro-user",JSON.stringify(res.data))
              ElMessage.success("員工注册成功")
              console.log(res)
              setTimeout(() => {
                location.href = '/login'
              }, 500)

            }else{
              ElMessage.error(res.msg)
            }

          })
        }
    })
  }
</script>

<template>
    <div class="login-container">

      <div class="login-box">
        <div style="padding: 50px 30px; background-color: white; margin-left: 100px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.2)">
          <el-form ref="formRef" :model="data.form" :rules="data.rules" style="width: 250px">
            <div style="text-align: center; margin-bottom: 20px; font-size: 20px; color: #0742b1 ; font-weight:bold" >
              欢迎注册
            </div>
            <el-form-item  prop="username" >
              <el-input size="large" v-model="data.form.username" placeholder="请输入账号" prefix-icon="UserFilled"/>
            </el-form-item>
            <el-form-item  prop="password" >
              <el-input show-password size="large"  v-model="data.form.password" placeholder="请输入密码" prefix-icon="Lock"/>
            </el-form-item>
            <el-form-item  prop="confirmPassword" >
              <el-input show-password size="large"  v-model="data.form.confirmPassword" placeholder="请再次确认密码" prefix-icon="Lock"/>
            </el-form-item>

            <div >
              <el-button @click="register" type="primary" size="large" style="width: 100%" >
                注册
              </el-button>
            </div>
            <div>
              <div style="text-align: right; margin-top: 10px; text-decoration: none">
                已有账号，请
                <a href="/login">
                  登录
                </a>
              </div>
            </div>

      </el-form>

      </div>

    </div>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/img/bg.jpg");
  background-size: cover;
  background-position: 0 -50px;
}

.login-box {
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  right: 0;
  position: absolute;
}
</style>