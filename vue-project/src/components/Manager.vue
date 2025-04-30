<script setup>

import {DArrowLeft, DataAnalysis, HomeFilled, Lock, Reading, SwitchButton} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive} from "vue";


const data = reactive({
  user: JSON.parse(localStorage.getItem("xm-pro-user")),
  form:{}
})


const logout = () =>{
  localStorage.removeItem("xm-pro-user") //清楚当前登录的用户缓存数据
  location.href = '/login'
}

const updateUser = () =>{
  data.form = JSON.parse(localStorage.getItem("xm-pro-user"))
}

</script>

<template>
<!--  头部区域  -->
  <div class="header" style="height: 60px; background-color: #535bf2; display: flex ;align-items: center">
    <div style="width : 200px ;; display: flex; align-items: center; margin-left: 10px">
      <img src="../assets/img/vue.svg" alt="" style="width: 40px; height: 40px;">
      <span style="font-size: 20px; display: flex; justify-content: space-around; align-items: center; color: white; margin-left: 10px" >
        后台管理系统
      </span>
    </div>
    <div style="flex:1"></div>
    <div style="width: fit-content ;display: flex ;align-items: center;margin: 10px">
        <img :src="data.form.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
        <span style="margin-left: 5px; color: white">{{ data.user.name }}</span>
    </div>
  </div>


  <div style="display:flex">
  <!--左侧菜单栏-->
    <div style="width: 200px ; border-right: 1px solid #dddddd; min-height : calc(100vh - 60px)">
      <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']">
        <el-menu-item index="/manager/home">
          <el-icon ><HomeFilled/></el-icon>
          系统首页
        </el-menu-item>
        <el-menu-item index="/manager/data">
          <el-icon><data-analysis/></el-icon>
          数据统计
        </el-menu-item>
        <el-menu-item index="/manager/article">
          <el-icon ><Reading/></el-icon>
          文章管理
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon><User/></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/manager/person">
          <el-icon ><SwitchButton/></el-icon>
          个人信息
        </el-menu-item>
        <el-menu-item index="/manager/password">
          <el-icon ><Lock/></el-icon>
          修改密码
        </el-menu-item>
        <el-menu-item @click = "logout">
          <el-icon ><SwitchButton/></el-icon>
          退出登录
        </el-menu-item>
      </el-menu>
    </div>
  <!--右侧主题区域-->
    <div style="flex: 1; width:0; background-color: #e3e3ff; padding: 10px" >
      <RouterView @updatePersonalInfo="updateUser" />
    </div>
  </div>

</template>

<style scoped>
.el-menu .is-active{
  background-color: #ECF5FF;
}

.el-sub-menu{
  background-color: white !important;
}
</style>