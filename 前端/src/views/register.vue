<template>
  <div style="margin-top: 30%"></div>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
        v-model="username"
        name="username"
        label="用户名"
        placeholder="请输入用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="userAccount"
        name="userAccount"
        label="账号"
        placeholder="账号为7到11位字母或数字组成"
        :rules="[{ required: true, message: '请填写账号' }]"
      />
      <van-field
        v-model="userPassword"
        type="password"
        name="userPassword"
        label="密码"
        placeholder="密码为7到11位字母或数字组成"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
      <van-field
        v-model="checkPassword"
        type="password"
        name="checkPassword"
        label="检验密码"
        placeholder="重复输入密码"
        :rules="[{ required: true, message: '请填写检验密码' }]"
      />
    </van-cell-group>

    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script>
import { showToast } from "vant";
import myAxios from "@/myAxios";
export default {
  data() {
    return {
      userAccount: null,
      userPassword: null,
      checkPassword: null,
      username: null,
    };
  },
  methods: {
    onSubmit() {
      myAxios
        .post("user/register", {
          userAccount: this.userAccount,
          userPassword: this.userPassword,
          checkPassword: this.checkPassword,
          username: this.username,
        })
        .then((res) => {
          console.log(res, "用户注册");
          if (res.code === 1) {
            showToast("注册成功");
            // 跳转到之前的页面
            this.$router.push({ name: "login" });
          } else {
            showToast("注册失败"+res.msg);
          }
        });
    },
  },
};
</script>

<style>
</style>