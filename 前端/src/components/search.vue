<template>
  <van-tree-select
    v-model:active-id="childTag"
    v-model:main-active-index="parentTag"
    :items="items"
    :max="8"
    height="100%"
    @click-item="onClick"
  />
</template>

<script>
import myAxios from "@/myAxios";
export default {
  props:['exitChildTag'],
  data() {
    return {
      childTag: [],
      parentTag: 0,
      items:[]
    };
  },
  created() {
    myAxios
      .get("/tag/get")
      .then((Response) => {
        this.items = Response.data;
      })
      .catch(function (error) {
        // 处理错误情况
        console.log(error);
      });
      if(this.exitChildTag!=undefined){
          this.childTag=this.exitChildTag
      }
  },

  methods: {
    onClick() {
      console.log(this.childTag)
      this.$emit("tagsValue", this.childTag);
    },
  },
};
</script>

<style>
</style>