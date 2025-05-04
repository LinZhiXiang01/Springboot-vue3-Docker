<script setup>
import router from "@/router/index.js";

const goHome=()=>{
  location.href = '/manager/home'

}


</script>

<template>
  <div style="text-align: center">
    <img alt="Vue logo" src="../assets/img/404.png" />
    <el-button type="primary" @click="router.push('/manager/home')" style="width: 80px;height: 60px">返回首页</el-button>
<!--    <el-button type="primary" @click="goHome" style="width: 80px;height: 60px">返回首页</el-button>-->

  </div>
</template>

<style scoped>

</style>