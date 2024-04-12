<template>
  <v-chart class="chart" :option="chartOption"/>
</template>

<script setup>
import {onMounted, ref, computed} from "vue";
import {getStatistics} from "~/api/page";

const data = ref('');

onMounted(() => {
  getTag();
});

const getTag = async () => {
  const res = await getStatistics();
  data.value = res.data;
};

const chartOption = computed(() => {
  return {
    title: {
      text: '兴趣伙伴匹配平台标签统计',
      subtext: '',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '兴趣伙伴匹配平台标签统计',
        type: 'pie',
        radius: '50%',
        data: data.value || [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
});
</script>

<style scoped>
.chart {
  height: 800px;
}
</style>
