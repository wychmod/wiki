<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic :value="statistic.viewCount" title="总阅读量">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic :value="statistic.voteCount" title="总点赞量">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic :precision="2" :value="statistic.voteCount / statistic.viewCount * 100"
                           :value-style="{ color: '#cf1322' }"
                           suffix="%"
                           title="点赞率">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic :value="statistic.todayViewCount" style="margin-right: 50px" title="今日阅读">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic :value="statistic.todayVoteCount" title="今日点赞">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                  :value="statistic.todayViewIncrease"
                  :value-style="{ color: '#0000ff' }"
                  title="预计今日阅读"
              >
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                  :precision="2"
                  :value="statistic.todayViewIncreaseRateAbs"
                  :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
                  class="demo-class"
                  suffix="%"
                  title="预计今日阅读增长"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row>
      <a-col :span="24">
        <div id="main" style="width: 100%;height: 300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import axios from 'axios';

declare let echarts: any;

export default defineComponent({
  name: 'the-welcome',
  setup () {
    const statistic = ref();
    statistic.value = {};
    const getStatistic = () => {
      axios.get('/ebook-snapshot/get-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticResp = data.content;
          console.log(statisticResp)

          statistic.value.viewCount = statisticResp[1].viewCount
          statistic.value.voteCount = statisticResp[1].voteCount
          statistic.value.todayViewCount = statisticResp[1].viewIncrease
          statistic.value.todayVoteCount = statisticResp[1].voteIncrease

          // 按分钟计算当前时间点，占一天的百分比
          const now = new Date();
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          // console.log(nowRate)
          statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
          // todayViewIncreaseRate：今日预计增长率
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[1].viewIncrease) / statisticResp[1].viewIncrease * 100;
          statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
       
        }
      });
    };


    const init30DayEcharts = (list: any) => {
      // 发布生产后出现问题：切到别的页面，再切回首页，报表显示不出来
      // 解决方法：把原来的id=main的区域清空，重新初始化
      const mainDom = document.getElementById('main-col');
      if (mainDom) {
        mainDom.innerHTML = '<div id="main" style="width: 100%;height:300px;"></div>';
      }
      // 基于准备好的dom，初始化echarts实例
      const myChart = echarts.init(document.getElementById('main'));

      const xAxis = [];
      const seriesView = [];
      const seriesVote = [];
      for (let i = 0; i < list.length; i++) {
        const record = list[i];
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      }

      // 指定图表的配置项和数据
      const option = {
        title: {
          text: '30天趋势图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总阅读量', '总点赞量']
        },
        grid: {
          left: '1%',
          right: '3%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '总阅读量',
            type: 'line',
            // stack: '总量', 不堆叠
            data: seriesView,
            smooth: true
          },
          {
            name: '总点赞量',
            type: 'line',
            // stack: '总量', 不堆叠
            data: seriesVote,
            smooth: true
          }
        ]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    };

    const get30DayStatistic = () => {
      axios.get('/ebook-snapshot/get-30-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticList = data.content;

          init30DayEcharts(statisticList)
        }
      });
    };

    onMounted(() => {
      getStatistic()
      get30DayStatistic()
    });

    return {
      statistic
    }
  }
});
</script>
