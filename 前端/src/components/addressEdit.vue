<template>
  <van-field
    v-model="fieldValue"
    is-link
    readonly
    label="地区"
    placeholder="请选择所在地区"
    @click="show = true"
  />
  <van-popup v-model:show="show" round position="bottom">
    <van-cascader
      v-model="cascaderValue"
      title="请选择所在地区"
      :options="options"
      @close="show = false"
      @finish="onFinish"
    />
  </van-popup>
</template>

<script>
import { useCascaderAreaData } from "@vant/area-data";
const areaData = useCascaderAreaData();
export default {
  data() {
    return {
      fieldValue: "",
      cascaderValue: "",
      show: false,
      options: areaData,
    };
  },
  methods: {
    onFinish({ selectedOptions }){
      this.show = false;
      this.fieldValue = selectedOptions.map((option) => option.text).join("/");
      this.$emit("addressValue",this.fieldValue)
    },
  },
};
</script>
<style>
</style>