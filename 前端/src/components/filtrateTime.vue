<template>
  <van-picker-group
    title="乘车时间范围"
    :tabs="['选择日期', '起始时间', '截至时间']"
    @confirm="onConfirm"
    @cancel="$emit('cancel')"
  >
    <van-date-picker
      v-model="currentDate"
      :columns-type="['month', 'day']"
      :formatter="formatter"
      :min-date="minDate"
      :max-date="maxDate"
    />
    <van-time-picker v-model="beginTime" />
    <van-time-picker v-model="endTime" />
  </van-picker-group>
</template>

<script>
export default {
  data() {
    return {
      minDate: undefined,
      maxDate: undefined,
      currentDate: undefined,
      beginTime: undefined,
      endTime: undefined,
    };
  },
  created() {
    let now = new Date();
    this.currentDate = [now.getMonth() + 1, now.getDate()];
    this.minDate = new Date();
    this.maxDate = new Date();
    this.maxDate.setMonth(now.getMonth() + 1);
    this.maxDate.setDate(1);
  },
  methods: {
    formatter: (type, option) => {
      if (type === "day") {
        option.text += "日";
      }
      if (type === "month") {
        option.text += "月";
      }
      return option;
    },
    onFiltrateTime() {
      if (this.showBottom == true) {
        this.showBottom = false;
      } else {
        this.showBottom = true;
      }
    },
    onConfirm() {
      console.log("确认");
      console.log(this.beginTime);
      console.log(this.endTime);
      console.log(this.currentDate);
      this.$emit('confirmTime',{
        currentDate:this.currentDate,
        beginDate:this.beginDate,
        endTime:this.endTime
      })
    }
  },
};
</script>

<style>
</style>