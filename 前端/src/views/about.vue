<template>
  <van-nav-bar
    left-text="返回"
    left-arrow
    @click-left="onClickLeft"
    title="关于"
  >
  </van-nav-bar>
  <div class="main">
    <div class="contact-info">
      <h1>本网站用于技术学习</h1>
      <p>我的邮箱: 2144547707@qq.com</p>
      <a href="https://beian.miit.gov.cn/" target="_blank"
        >ICP备案号: 蜀ICP备2024074931号-1</a
      >
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    //点击返回
    onClickLeft: () => history.back(),
  },
};
</script>

<style>
.main {
  display: flex;
  justify-content: center;
  height: 100vh;
  margin: 0;
}

.contact-info {
  text-align: center;
}
</style>