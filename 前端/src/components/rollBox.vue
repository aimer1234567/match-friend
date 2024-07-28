  <template>
  <nut-infinite-loading
    v-model="infinityValue"
    :has-more="hasMore"
    @load-more="loadMore"
  >
  <van-card
  :key="user"
  v-for="user in 7"
  :title="user"
  thumb="https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg"
>
  <template #tags>
    <van-tag plain type="primary">标签</van-tag>
    <van-tag plain type="primary">标签</van-tag>
  </template>
  <template #footer>
    <van-button size="mini">按钮</van-button>
    <van-button size="mini">按钮</van-button>
  </template>
</van-card>
  </nut-infinite-loading>
</template>


<script>
export default {
  data() {
    return {
      infinityValue: false,
      hasMore: true,
    }
  },
};
</script>

<style>
</style>