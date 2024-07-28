<template>
  <div style="height: 100vh; position: relative">
    <van-nav-bar
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      :title="`${information.username}的主页`"
    >
    </van-nav-bar>
    <van-cell-group>
      <van-cell title="头像">
        <template #title>
          <van-image
            fit="cover"
            position="left"
            width="7rem"
            height="7rem"
            :src="information.avatarUrl"
          />
        </template>
      </van-cell>
      <van-cell title="性别" :value="genderString" />
      <van-cell title="出生日" :value="information.age" />
      <van-cell title="地址" :value="information.address" />
      <van-cell title="标签">
        <template #extra>
          <van-space :size="10" wrap>
            <van-tag
              v-for="tag in information.tags"
              :key="tag"
              type="primary"
              size="medium"
              >{{ tag }}</van-tag
            >
          </van-space>
        </template>
      </van-cell>
      <van-cell title="社交问题" :value="information.problem" />
      <van-cell title="自我介绍" :value="information.introduction" />
    </van-cell-group>
    <van-button type="primary" @click="addFriend" block>获取联系方式</van-button>
  </div>
</template>

<script>
import myAxios from "@/myAxios";
import axios from "@/myAxios";
import { showToast } from "vant";
import { showDialog } from 'vant';
export default {
  data() {
    return {
      information: {
        userAccount: this.$route.params.userAccount,
        username: null,
        avatarUrl: null,
        gender: null,
        age: null,
        address: null,
        introduction: null,
        problem: null,
      },
    };
  },
  computed: {
    genderString() {
      if (this.information.gender == 1) {
        return "女";
      } else {
        return "男";
      }
    },
  },
  created() {
    let path = "user/space/" + this.$route.params.userAccount;
    axios.get(path).then((res) => {
      if (res.code == 1) {
        this.information = res.data;
        this.information.tags = JSON.parse(this.information.tags);
      } else {
        showToast("错误：" + res.msg);
      }
    });
  },
  methods: {
    onClickLeft() {
      history.back();
    },
    addFriend() {
      if (this.information.problem === "无") {
        myAxios
        .get("/message/send", {
          params:{
            acceptUserAccount:this.information.userAccount
          }
        })
        .then((res) => {
          if (res.code === 1) {
            showDialog({ message: `微信号为：${res.data.wchat}` });
          } else {
            showToast("请求发送失败");
          }
        });
      } else {
        this.$router.push({
          name: "addFriend",
          params: this.information.userAccount,
          query: {
            problem: this.information.problem,
            name: this.information.username,
            userAccount: this.information.userAccount,
          },
        });
      }
    },
  },
};
</script>

<style>
</style>
