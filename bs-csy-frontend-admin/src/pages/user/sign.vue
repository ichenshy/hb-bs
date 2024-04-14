<template>
  <el-table :data="tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号"/>
    <el-table-column prop="authName" label="签到人"/>
    <el-table-column prop="isBackupName" label="是否补签"/>
    <el-table-column prop="fraction" label="积分数"/>
    <el-table-column prop="signDate" label="签到时间"/>
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
import {blogByAdmin, disabledTeam, joinUser, signByAdmin, teamPageByAdmin} from "~/api/page";
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
  const res = await signByAdmin(currentPage, searchText);
  console.log(res)
  tableData.value = res.data.records.map(record => ({
    ...record,
    isBackupName: record.isBackup == 0 ? '否' : '是',
    authName: record.user.username
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

