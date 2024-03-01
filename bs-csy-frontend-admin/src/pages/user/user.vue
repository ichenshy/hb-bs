<template>
  <div>
    <el-card>
      <el-descriptions class="mt-0" :title="`个人简介 `" :column="2" border>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <Picture/>
            </el-icon>
            头像
          </template>
          <el-avatar shape="square" :size="100" :src="$store.state.user.avatarUrl"/>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <User/>
            </el-icon>
            账户名
          </template>
          {{ $store.state.user.userAccount }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <Avatar/>
            </el-icon>
            昵称
          </template>
          {{ $store.state.user.username }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <Message/>
            </el-icon>
            电话
          </template>
          {{ $store.state.user.phone }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <Location/>
            </el-icon>
            登陆IP
          </template>
          {{ ipData.ip }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <Location/>
            </el-icon>
            角色
          </template>
          {{ $store.state.user.role == 1 ? '管理员' : '用户' }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <el-icon>
              <ChatRound/>
            </el-icon>
            个人简介
          </template>
          {{ $store.state.user.profile }}
        </el-descriptions-item>
      </el-descriptions>
      <el-button class="mt-2" type="primary" @click="UpData_Button($store.state.user)">修改</el-button>
    </el-card>
    <FormDialog ref="DialogformRef" :title="Formtitle" @submit="FormSubmit" width="40%" :Buttontitle="Buttontitle">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="80px">
        <div class="flex justify-between">
          <el-form-item prop="avatar" label="头像">
            <el-input v-model="form.avatarUrl" placeholder="头像地址"></el-input>
          </el-form-item>
          <el-form-item prop="username" label="用户名">
            <el-input v-model="form.userAccount" placeholder="用户名"></el-input>
          </el-form-item>
        </div>
        <div class="flex justify-between">
          <el-form-item prop="name" label="昵称">
            <el-input v-model="form.username" placeholder="昵称"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="电话">
            <el-input v-model="form.phone" placeholder="电话"></el-input>
          </el-form-item>
        </div>
        <div class="flex justify-between">
          <el-form-item prop="job" label="角色">
            <el-input v-model="form.role" placeholder="角色"></el-input>
          </el-form-item>
          <el-form-item prop="text" label="个人简介">
            <el-input v-model="form.profile" placeholder="个人简介" type="textarea"></el-input>
          </el-form-item>
        </div>
      </el-form>
    </FormDialog>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ip} from '~/api/public';
import FormDialog from "~/components/FormDialog.vue";
import {InitForm} from '~/composables/Form.js'
import {updateuser} from '~/api/manager.js'
import store from '~/store'


const ipData = ref({
  ip: "",
  system: ""
})
ip().then(res => {
  console.log(res.location)
  ipData.value = res.data

})
const {
  form,
  // 表单验证规则
  rules,
  // 父级FormDialog的ref
  DialogformRef,
  // 表单el-form的ref
  formRef,
  // 表单按钮标题
  Buttontitle,
  // 表单标题
  Formtitle,
  UpData_Button,
  FormSubmit
} = InitForm({
  title: "个人中心",
  // 搜索表单
  // searchKeyword: "",
  form: {
    id: store.state.user.uid,
    avatar: "",
    username: "",
    name: "",
    email: "",
    job: "",
    text: ""
  },
  Buttontitle_value: {
    title_1: '修改数据',
  },
  Formtitle_value: {
    title_1: '修改用户数据',
  },
  rules: {
    avatar: [{required: true, message: '头像地址不能为空', trigger: 'blur'}],
    username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
    name: [{required: true, message: '昵称不能为空', trigger: 'blur'}],
    email: [{required: false, message: '邮箱可以为空', trigger: 'blur'}],
    job: [{required: false, message: '职业可以为空', trigger: 'blur'}],
    text: [{required: false, message: '个性签名可以为空', trigger: 'blur'}]
  },
  getdata: ip,
  updata: updateuser,
})
</script>
