<template>
  <div>
    <el-form :inline="true" :model="searchText" class="demo-form-inline">
      <el-form-item label="模糊匹配">
        <el-input v-model="searchText" placeholder="根据备注查询" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-table :data="tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号"/>
    <el-table-column prop="fromName" label="发送用户账号"/>
    <el-table-column prop="receiveName" label="接受用户账号"/>
    <el-table-column prop="isReadName" label="是否已读"/>
    <el-table-column prop="statusName" label="申请状态"/>
    <el-table-column prop="remark" label="备注"/>
    <el-table-column prop="createTime" label="申请时间"/>
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
import {friendByAdmin} from "~/api/page";

const tableData = ref([])
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchText = ref('')
const onSubmit = () => {
  getList(1, searchText.value)
}
const getList = async (currentPage, searchText) => {
  const res = await friendByAdmin(currentPage, searchText);
  tableData.value = res.data.records.map(record => ({
    ...record,
    isReadName: record.isRead === 0 ? '未读' : '已读',
    statusName: getStatusName(record.status)
  }));
  total.value = res.data.total;
};
const getStatusName = (status) => {
  switch (status) {
    case 0:
      return '未通过';
    case 1:
      return '已同意';
    case 2:
      return '已过期';
    case 3:
      return '不同意';
    default:
      return '未知状态';
  }
};
onMounted(() => {
  getList(1, '');
});
const handleCurrentChange = (page) => {
  currentPage.value = page;
  getList(currentPage.value, '');
};


</script>
