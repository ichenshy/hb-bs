<template>
  <div>
    <el-row :gutter="14" class="pt-4">
      <el-col :span="12">
        <el-card class="h-65">
          <template #header>
            <div>
              <span>欢迎光临</span>
              <el-button class="button" text>
                <el-tag type="primary"> {{ $store.state.user.username }}</el-tag>
              </el-button>
            </div>
          </template>
          <div class="welcome-message">
            <p>今天是{{ date }}</p>
            <p>感谢您来访我们的网站！</p>
            <p>在这里，我们致力于为您带来创新、令人惊叹的体验。</p>
            <p>请随意浏览我们的内容，希望您会喜欢！</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="h-65">
          <template #header>
            <div>
              <span>心灵鸡汤</span>
            </div>
          </template>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="h-65">
          <template #header>
            <div>
              <span>系统作者</span>
              <el-button class="button" text>
                <el-tag>陈胜源</el-tag>
              </el-button>
            </div>
          </template>
          <div class="dirtext">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="Github">http://github.com/ichenshy</el-descriptions-item>
              <el-descriptions-item label="昵称">chenShengYuan</el-descriptions-item>
              <el-descriptions-item label="联系方式" :span="2">18364821270</el-descriptions-item>
              <el-descriptions-item label="爱好" :span="2">
                <el-tag class="mr-1">摸鱼🐟</el-tag>
                <el-tag class="mr-1">写代码🐟</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="h-65">
          <div class="dirtextlist">

            <el-descriptions title="技术栈" :column="3" border>
              <el-descriptions-item label="SpringCloud" label-align="right">
                <el-tag type="danger">^2.7.2</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="SpringSession" label-align="right">
                <el-tag>^2.7.2</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="MySQL" label-align="right">
                <el-tag type="danger">^5.7</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Redis" label-align="right">
                <el-tag type="danger">^6.0</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="MyBatisPlus" label-align="right">
                <el-tag type="danger">^3.5.1</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Vue3" label-align="right">
                <el-tag>^3.2.25</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Element-Plus" label-align="right">
                <el-tag>^2.2.27</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Echarts" label-align="right">
                <el-tag type="danger">^5.3.3</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Vite" label-align="right">
                <el-tag>^4.0.3</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="AliyunSDK" label-align="right">
                <el-tag>^3.1.0</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Gson" label-align="right">
                <el-tag>^2.8.9</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Hutool" label-align="right">
                <el-tag>^5.7.17</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {getDateTime} from '~/api/public'

const date = getDateTime()

</script>
<style scoped>
.dirtext {
  width: 100%;
}

.dirtextlist {
  width: 100%;
}


.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

</style>
