<template>
  <van-nav-bar title="寻找" left-text="查看申请" @click-left="toApply" @click-right="onFilter">
    <template #right>
      <van-icon name="search" size="18" />
    </template>
  </van-nav-bar>
  <!-- 排序 -->
  <van-dropdown-menu>
    <van-dropdown-item
      v-model="sort"
      :options="sortOption"
      @change="onSearch"
    />
  </van-dropdown-menu>
  <!-- 下拉刷新 -->
  <van-pull-refresh v-model="refresh" @refresh="refreshFun">
    <!-- 用户信息-->
    <van-card
      :key="user"
      v-for="user in userList"
      :title="user.username"
      :thumb="user.avatarUrl"
    >
      <!-- 年龄和位置信息 -->
      <template #desc>
        <van-row gutter="20">
          <van-col v-if="user.address !== null">
            {{ user.address }}
          </van-col>
          <van-col v-if="user.age !== null"> {{ user.age }}年 </van-col>
        </van-row>
      </template>
      <!-- 标签 -->
      <template #tags>
        <van-space :size="10" wrap>
          <van-tag
            v-for="tag in user.tags"
            :key="tag"
            type="primary"
            size="medium"
            >{{ tag }}</van-tag
          >
        </van-space>
      </template>
      <!-- 查看用户详情 -->
      <template #footer>
        <router-link :to="`/userSpace/${user.userAccount}`"
          >查看详情</router-link
        >
      </template>
    </van-card>
    <van-back-top @click="onSearch" />
  </van-pull-refresh>
  <van-divider dashed>到底啦，点击箭头更新</van-divider>

  <!-- 圆角弹窗（底部） -->
  <van-popup
    v-model:show="showFilter"
    round
    closeable
    @click-close-icon="onSearch"
    :close-on-click-overlay="false"
    position="bottom"
    :style="{ height: '70%' }"
  >
    <template #overlay-content>
      <tags :value="tagNameArray"></tags>
    </template>
    <search @tagsValue="onTagsValue"></search>
  </van-popup>
</template>

<script>
import search from "@/components/search.vue";
import tags from "@/components/tags.vue";
import myAxios from "@/myAxios";
import { showToast } from "vant";
import qs from "@/qs";
import Apply from "./apply.vue";
export default {
  name: "index",
  components: {
    search,
    tags,
  },
  data() {
    return {
      refresh: false,
      sort: "similarSort", //选择的排序方式
      sortOption: [
        { text: "相似度优先", value: "similarSort" },
        { text: "同龄人优先", value: "ageSort" },
        { text: "附近的人优先", value: "addressSort" },
      ],
      showFilter: false, //是否展示分类界面
      tagNameArray: [], //筛选标签
      userList: [], //搜索到的用户列表
    };
  },
  created() {
    this.onSearch();
  },
  methods: {
    toApply(){
      this.$router.push({name:"apply"})
    },
    //打开底部弹窗
    onFilter() {
      this.showFilter = !this.showFilter;
    },
    //点击筛选，传输选中的值
    onTagsValue(value) {
      this.tagNameArray = value;
    },
    //搜索
    onSearch() {
      myAxios
        .get("/user/search/tags", {
          params: {
            tagNameArray: this.tagNameArray,
            pageSize: 20,
            sortMethod: this.sort,
          },
          paramsSerializer: function (params) {
            return qs.stringify(params, { indices: false });
          },
        })
        .then((Response) => {
          if (Response.code == 1) {
            this.userList = Response.data;
            if (this.userList.length === 0) {
              return;
            }
            for (let user of this.userList) {
              user.tags = JSON.parse(user.tags);
            }
          } else {
            showToast(Response.msg);
          }
        });
    },
    refreshFun() {
      this.onSearch();
      console.log(this.userList);
      setTimeout(() => {
        this.refresh = false;
      }, 1000);
    },
  },
};
</script>

<style>
</style>