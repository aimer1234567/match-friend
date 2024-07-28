<template>
  <div style="height: 100vh; position: relative">
    <van-nav-bar
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      :title="`${$route.query.name}的主页`"
    ></van-nav-bar>
    <van-cell title="社交问题" :value="$route.query.problem" />
    <van-cell-group inset>
      <van-field
        v-model="answer"
        rows="2"
        autosize
        label="我的答案"
        type="textarea"
        maxlength="50"
        placeholder="请输入"
        show-word-limit
      />
    </van-cell-group>
    <div style="margin: 16px">
      <van-button
        round
        block
        type="primary"
        @click="addFriend"
        native-type="submit"
      >
        提交
      </van-button>
    </div>
  </div>
</template>

<script>
import { showToast } from "vant";
import myAxios from "@/myAxios";
export default {
  data() {
    return {
        answer:null,
    };
  },
  methods: {
    addFriend() {
      myAxios
        .post("/message/send/apply", {
          acceptUserAccount: this.$route.query.userAccount,
          answer: this.answer,
          problem: this.$route.query.problem,
        }).then(res=>{
          if(res.code!==1){
            showToast(res.msg)
          }
        }
        )
    },
    onClickLeft() {
      history.back();
    },
  },
};
</script>

<style>
</style>