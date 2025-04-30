<script setup>

import {reactive, ref} from "vue";
import {Delete} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const test = () => {
  alert("test")
}
const data = reactive({
  id:router.currentRoute.value.query.id,
  name:router.currentRoute.value.query.name,
  a: 1,
  b:"hahaha",
  fruit:["apple","banana","orange"],

  employeeList: []
})
const a = ref(1)


request.get("employee/selectAll").then(res=>{

  data.employeeList = res.data
  console.log(data.employeeList)

})

console.log("成功捕获："+router.currentRoute.value.query.id+router.currentRoute.value.query.name)
</script>

<template>
  <div>
      <RouterLink to="/test">go to Test</RouterLink>
      <el-button type="primary" @click="router.push('/test')">
          push to Test
      </el-button>
  </div>
  <div>
    <el-button type = "danger" :icon="Delete" circle @click="test">

    </el-button>
    <el-icon>
      <Edit />
    </el-icon>
    <el-icon :size="40" style="color:red ;top :10px"><View /></el-icon>100

  </div>
  <div style="margin:30px">
    <el-button @clikc="test" plain>外侧？？？？</el-button>
    <el-button type="warning">Warning</el-button>
  </div>

  <div style="font-weight:bold ;color: red">
    {{ data.a }}
    {{data.b}}
  </div>
  <div>
    <input type="text" v-model="data.a">
  </div>

  <div >
    <option v-for="item in data.fruit">
      {{item}}
    </option>
    <h1 v-on:click=test>{{data.a}}</h1>
    <img :src="data.img" alt="">
  </div>




</template>

<style scoped>


</style>