<template>
<router-view v-slot="{Component, route}">
  <keep-alive>
    <component :is="Component" :key="route.path + (route.meta.keepAlive ? '' : Math.random())" />
  </keep-alive>
</router-view>
  <van-tabbar v-model="active" @change="onChange">
    <van-tabbar-item name="index" icon="home-o">首页</van-tabbar-item>
    <van-tabbar-item name="apply" icon="search">申请</van-tabbar-item>
    <van-tabbar-item name="user" icon="friends-o">个人</van-tabbar-item>
  </van-tabbar>
  
</template>

<script>
import myAxios from "@/myAxios";
import { showToast } from "vant";
import { showNotify } from "vant";
export default {
  data() {
    return {
      active: this.$route.path,
    };
  },
  methods: {
    onChange() {
      this.$router.push({ name: this.active });
    },
    handleMessage(event) {
      if(event.data==='1'){
      showNotify("你有新的申请");
      }else if(event.data==='2'){
        showNotify("你有一条申请通过被拒绝");
      }else{
        showNotify("你有一条申请通过");
      }
    },
  },
  mounted() {
    this.$store.commit("seeConnector");
    this.$store.state.sse.onmessage = this.handleMessage;
  },
  beforeDestroy() {
    // 在组件销毁之前关闭 EventSource 连接
    this.$store.state.sse.close();
    console.log("ssssssss")
    myAxios.get("/user/closeSse");
  },
};
</script>

<style>
</style>