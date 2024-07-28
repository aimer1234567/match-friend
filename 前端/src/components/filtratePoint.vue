<template>
  <van-picker-group
    title="乘车起点目的地选择"
    :tabs="['起点', '目的地']"
    @confirm="onConfirm"
    @cancel="$emit('cancel')"
  >
    <van-picker
      v-model="startPoint"
      :columns="startColumns"
    />
    <van-picker
      v-model="endPoint"
      :columns="endColumns"
    />
  </van-picker-group>
</template>

<script>
export default {
  data() {
    return {
      startPoint:undefined,
      endPoint:undefined,
      startColumns: [
        { text: "旗山北区", value: "旗山北区" },
        { text: "旗山南区", value: "旗山南区" },
        { text: "鼓山校区", value: "鼓山校区" },
        { text: "长乐机场", value: "长乐机场" },
        { text: "福州站", value: "福州站" },
        { text: "福州南站", value: "福州南站" },
      ],
      endColumns: [
        { text: "旗山北区", value: "旗山北区" },
        { text: "旗山南区", value: "旗山南区" },
        { text: "鼓山校区", value: "鼓山校区" },
        { text: "长乐机场", value: "长乐机场" },
        { text: "福州站", value: "福州站" },
        { text: "福州南站", value: "福州南站" },
      ],
    };
  },
  created() {},
  methods: {
    onConfirm() {
      console.log("确认");
      console.log(this.beginTime);
      console.log(this.endTime);
      console.log(this.currentDate);
      this.$emit("confirmPoint", {
        startPoint:this.startPoint,
        endPoint:this.endPoint,
      });
    },
  },
};
</script>

<style>
</style>