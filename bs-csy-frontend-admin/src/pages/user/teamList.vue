<template>
  <div>
    <el-form :inline="true" :model="searchText" class="demo-form-inline">
      <el-form-item label="模糊匹配">
        <el-input v-model="searchText" placeholder="根据队伍名称与描述查询" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-table :data="tableData" stripe style="width: 100%">
    <el-table-column prop="id" label="编号"/>
    <el-table-column prop="name" label="队伍名称"/>
    <el-table-column prop="description" label="队伍描述"/>
    <el-table-column prop="maxNum" label="最大人数"/>
    <el-table-column prop="coverImage" label="封面">
      <template #default="{ row }">
        <img :src="row.coverImage" style="width: 50px; height: 50px; border-radius: 50%;" alt=""/>
      </template>
    </el-table-column>
    <el-table-column prop="expireTimeName" label="过期时间"/>
    <el-table-column prop="createUserName" label="创建人"/>
    <el-table-column prop="statusName" label="性质"/>
    <el-table-column prop="passwordName" label="密码"/>
    <el-table-column prop="hasJoinNum" label="已加入人数"/>
    <el-table-column prop="createTime" label="创建时间"/>
    <el-table-column label="操作">
      <template #default="{ row }">
        <el-button-group>
          <el-button type="primary" @click="viewMembers(row)">查看加入人员</el-button>
          <el-button type="danger" @click="deleteTeam(row)">删除队伍</el-button>
        </el-button-group>
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

  <!-- 模态框 -->
  <el-dialog title="已加入的用户" v-model='dialogVisible' width="80%">
    <el-table :data="userData" stripe style="width: 100%">
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
    </el-table>
  </el-dialog>

</template>
<script setup>
import {onMounted, ref} from 'vue';
import {disabledTeam, joinUser, teamPageByAdmin} from "~/api/page";
import {ElMessageBox} from "element-plus";
import {toast} from "~/composables/util";

const tableData = ref([])
const currentPage = ref(1);
const pageSize = ref(10);
const userData = ref([])
const total = ref(0);
const searchText = ref('')
const dialogVisible = ref(false);
const onSubmit = () => {
  getList(1, searchText.value)
}
const getList = async (currentPage, searchText) => {
  const res = await teamPageByAdmin(currentPage, searchText);
  console.log(res)
  tableData.value = res.data.records.map(record => ({
    ...record,
    statusName: record.status === 0 ? '公开' : '私密',
    passwordName: record.password === '' ? '无密码' : record.password,
    expireTimeName: record.expireTime === null ? '永不过期' : record.expireTime,
    createUserName: record.createUser.username
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
const deleteTeam = (row) => {
  ElMessageBox.alert(`确定要删除${row.name}队伍吗`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 点击确定后执行删除操作
    disTeam(row);
  });
};
const disTeam = (row, status) => {
  console.log(status)
  const res = disabledTeam(row.id, status);
  if (res) {
    toast("成功", "success")
    getList(1);
  }
}

const viewMembers = async (row) => {
  userData.value = []
  dialogVisible.value = true; // 打开模态框
  const res = await joinUser(row.id)
  userData.value = res.data.map(record => ({
    ...record,
    role: record.role === 1 ? '管理员' : '用户',
    gender: record.gender === 1 ? '男' : '女',
    statusName: record.status === 1 ? '禁用' : '启用',
    tags: JSON.parse(record.tags)
  }));
}

</script>

<style scoped>
/* 添加样式以确保按钮水平排列 */
.el-button-group {
  display: inline;
  justify-content: space-between;
}

.el-button {
  margin-top: 10px;
  width: 100px;
  height: 30px
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}
</style>
