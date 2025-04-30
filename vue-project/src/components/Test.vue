<script setup>
import router from "@/router/index.js";


const data = {

};

</script>

<template>
  <div>welcome</div>
  <el-button type="primary" @click="router.push('/home')">
    push to home
  </el-button>

  <el-button type="primary" @click="router.push('/home?id=1')">
    路由传参 id=1
  </el-button>

  <el-button type="primary" @click="router.push({path:'/home', query:{id:1,name:'mocha'}})">
    路由传参 id=1
  </el-button>
</template>

<style scoped>

</style>