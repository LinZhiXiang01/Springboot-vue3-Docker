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



</script>

<template>



</template>

<style scoped>


</style>