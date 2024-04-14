<template>
  <div>
    <el-form :inline="true" :model="searchText" class="demo-form-inline">
      <el-form-item label="模糊匹配">
        <el-input v-model="searchText" placeholder="根据用户名昵称与手机号查询" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-table :data="tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号"/>
    <el-table-column prop="userAccount" label="账号"/>
    <el-table-column prop="username" label="昵称"/>
    <el-table-column prop="gender" label="性别"/>
    <el-table-column prop="avatarUrl" label="头像">
      <template #default="{ row }">
        <img :src="row.avatarUrl" style="width: 50px; height: 50px; border-radius: 50%;" alt=""/>
      </template>
    </el-table-column>
    <el-table-column prop="email" label="邮箱"/>
    <el-table-column prop="phone" label="手机号"/>
    <el-table-column prop="profile" label="描述"/>
    <el-table-column prop="role" label="角色"/>
    <el-table-column prop="tags" label="标签">
      <template #default="{ row }">
        <div class="tag-list">
          <el-tag v-for="(tag, index) in row.tags" :key="index">{{ tag }}</el-tag>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="statusName" label="是否禁用"/>
    <el-table-column label="操作">
      <template #default="{ row}">
        <el-button v-if="row.status === 1" type="success" @click="disabled(row, 0)">启用</el-button>
        <el-button v-else type="danger" @click="disabled(row, 1)">禁用</el-button>
      </template>
    </el-table-column>
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
import {onMounted, reactive, ref} from 'vue';
import {disabledUser, getUserList} from "~/api/page";
import {ElMessageBox} from "element-plus";
import {toast} from "~/composables/util";

const tableData = ref([])
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchText = ref('')
const onSubmit = () => {
  getList(1, searchText.value)
}
const getList = async (currentPage, searchText) => {
  const res = await getUserList(currentPage, searchText);
  tableData.value = res.data.records.map(record => ({
    ...record,
    role: record.role === 1 ? '管理员' : '用户',
    gender: record.gender === 1 ? '男' : '女',
    statusName: record.status === 1 ? '禁用' : '启用',
    tags: JSON.parse(record.tags)
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
const disabled = (row, status) => {
  ElMessageBox.alert(`确定要禁用账号为${row.userAccount}的用户吗`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 点击确定后执行删除操作
    disUser(row, status);
  });
};
const disUser = (row, status) => {
  console.log(status)
  const res = disabledUser(row.id, status);
  if (res) {
    toast("成功", "success")
    getList(1);
  }

}

</script>

<style scoped>
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>
