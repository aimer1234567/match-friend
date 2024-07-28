<template>
  <van-row>
    <van-col  offset="3">
      <img
        src="@/assets/friend.jpg"
        alt="Image"
        style="margin: auto; height: 200px"
      />
    </van-col>
  </van-row>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
        v-model="userAccount"
        name="userAccount"
        label="账号"
        placeholder="请输入账号"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="userPassword"
        type="password"
        name="userPassword"
        label="密码"
        placeholder="请输入密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        登录
      </van-button>
    </div>
  </van-form>
  <div style="margin-right: 10%">
    <van-row justify="end" gutter="20">
      <router-link to="/register"><van-col>注册</van-col> </router-link>
    </van-row>
  </div>
  <van-row>
    <van-col span="10" offset="8">
      <div style="position: absolute; bottom: 8vh">by LuoXiaoying</div>
    </van-col>
  </van-row>
  <van-row>
    <van-col span="10" offset="6">
      <div style="position: absolute; bottom: 5vh">
        <a href="https://beian.miit.gov.cn/" target="_blank" style="color: grey"
          >蜀ICP备2024074931号-1</a
        >
      </div>
    </van-col>
  </van-row>
</template>

<script>
import { showToast } from "vant";
import myAxios from "@/myAxios";
export default {
  data() {
    return {
      userAccount: null,
      userPassword: null,
    };
  },
  methods: {
    onSubmit() {
      myAxios
        .post("user/login", {
          userAccount: this.userAccount,
          userPassword: this.userPassword,
        })
        .then((res) => {
          console.log(res, "用户登录");
          if (res.code === 1) {
            showToast("登录成功");
            // 跳转到之前的页面
            localStorage.setItem("token", res.data);
            this.$router.push({ name: "index" });
          } else {
            showToast("登录失败:" + res.msg);
          }
        });
    },
  },
};
</script>

<style>
</style>