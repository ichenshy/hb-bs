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
              <Message/>
            </el-icon>
            邮件地址
          </template>
          {{ $store.state.user.email }}
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
          {{ $store.state.user.role === 1 ? '管理员' : '用户' }}
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
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ip} from '~/api/public';
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
const imageUrl = ref('')
const {
  form,
  // 父级FormDialog的ref
  DialogformRef,
  // 表单el-form的ref
  formRef,
  // 表单按钮标题
  Formtitle,
} = InitForm({
  title: "个人中心",
  form: {
    id: store.state.user.uid,
    avatarUrl: "",
    userAccount: "",
    username: "",
    phone: "",
    email: "",
    profile: ""
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


<style scoped>
.avatar-uploader .avatar {
  width: 70px;
  height: 70px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

</style>
