<script setup>

import {reactive} from "vue";
import router from "@/router/index.js";

const test = () => {
  alert("test")
}
const data = reactive({
  id:router.currentRoute.value.query.id,
  name:router.currentRoute.value.query.name
})



</script>

<template>



</template>

<style scoped>


</style>