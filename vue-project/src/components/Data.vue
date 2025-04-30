<script setup>
import {reactive} from "vue";

const data = reactive({
  name:null,
  table:[
    {  id: 1, date: '2024-10-11', name: '亲哥哥哥', address: '安徽合肥' },
    {  id: 2, date: '2024-10-12', name: '小鲁班', address: '上海浦东' },
    {  id: 3, date: '2024-10-13', name: '小妲己', address: '北京大兴' },
  ],
  pageNum:1,
  pageSize:10,
  total:0,
})
</script>

<template>
  <div>
    <div class = "card" style="margin-bottom: 5px">
        <el-input style="width: 240px; margin-right:20px" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
        <el-button type="primary">查询</el-button>
        <el-button type="warning">重置</el-button>
    </div>
    <div class = "card" style="margin-bottom: 5px">
      <el-button type="primary">新增</el-button>
      <el-button type="warning">批量删除</el-button>
      <el-button type="info">导入</el-button>
      <el-button type="success">导出</el-button>

    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.table" stripe border>
        <el-table-column label="日期" prop="date"/>
        <el-table-column label="名称" prop="name"/>
        <el-table-column label="地址" prop="address"/>
      </el-table>
      <div style="margin-top: 10px">
        <el-pagination
            v-mo  del:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 15, 20]"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.total"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>