<script setup>

import {reactive} from "vue";
import router from "@/router/index.js";

const test = () => {
  alert("test")
}
const data = reactive({
  authId:router.currentRoute.value.query.authId,
  name:router.currentRoute.value.query.name
})



</script>

<template>



</template>

<style scoped>


</style>