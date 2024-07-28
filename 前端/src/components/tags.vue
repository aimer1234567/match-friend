<template>
  <van-divider dashed style="margin-top: 20%">已选标签</van-divider>
  <van-space :size="20" wrap>
    <van-tag
      :key="tag"
      v-for="tag in value"
      size="large"
      type="primary"
    >
      {{ tag }}
    </van-tag>
  </van-space>
</template>

<script>
export default {
  props: ['value'],
};
</script>

<style>
</style>