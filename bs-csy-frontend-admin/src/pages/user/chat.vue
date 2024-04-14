<template>
  <div>
    <el-form :inline="true" :model="searchText" class="demo-form-inline">
      <el-form-item label="模糊匹配">
        <el-input v-model="searchText" placeholder="根据内容查询" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-table :data="tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号"/>
    <el-table-column prop="fromName" label="发送人"/>
    <el-table-column prop="toName" label="接收人"/>
    <el-table-column prop="text" label="内容"/>
    <el-table-column prop="teamName" label="接收群"/>
    <el-table-column prop="chatTypeName" label="聊天类型"/>
    <el-table-column prop="createTime" label="创建时间"/>
  </el-table>
  <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next, jumper"
  />

</template>
<script setup>
import {onMounted, ref} from 'vue';
import {blogByAdmin, chatByAdmin, disabledTeam, joinUser, teamPageByAdmin} from "~/api/page";
import {ElMessageBox} from "element-plus";
import {toast} from "~/composables/util";

const tableData = ref([])
const currentPage = ref(1);
const pageSize = ref(10);
const userData = ref([])
const total = ref(0);
const searchText = ref('')
const onSubmit = () => {
  getList(1, searchText.value)
}
const getList = async (currentPage, searchText) => {
  const res = await chatByAdmin(currentPage, searchText);
  console.log(res)
  tableData.value = res.data.records.map(record => ({
    ...record,
    fromName: record.fromUser.username,
    toName: record.toUser.username === null ? '' : record.toUser.username,
    teamName: record.teamVO.name === null ? '' : record.teamVO.name,
    chatTypeName: record.chatType === 1 ? '私聊' : '群聊',

  }));
  total.value = res.data.total;
};

onMounted(() => {
  getList(1, '');
});
const handleCurrentChange = (page) => {
  currentPage.value = page;
  getList(currentPage.value, '');
};

</script>
