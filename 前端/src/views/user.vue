<template>
  <van-nav-bar
    title="个人"
    left-text="关于网站"
    @click-left="onClickLeft"
    right-text="退出登录"
    @click-right="quit"
  >
  </van-nav-bar>
  <van-cell-group>
    <van-cell
      title="头像"
      is-link
      @click="onClick('avatarUrl', '名称', information.avatarUrl)"
    >
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
    <van-cell
      title="名称"
      :value="information.username"
      is-link
      @click="onClick('username', '名称', information.username)"
    />
    <van-cell title="账号" :value="information.userAccount" />
    <van-cell
      title="性别"
      :value="genderString"
      is-link
      @click="onClick('gender', '性别', information.gender)"
    />
    <van-cell
      title="出生日"
      :value="information.age"
      is-link
      @click="onClick('age', '出生日', information.age)"
    />
    <van-cell
      title="地址"
      :value="information.address"
      is-link
      @click="onClick('address', '地址', information.address)"
    />
    <van-cell
      title="电话"
      :value="information.phone"
      is-link
      @click="onClick('phone', '电话', information.phone)"
    />

    <van-cell
      title="微信"
      :value="wchatComputed"
      is-link
      @click="onClick('wchat', '微信', information.wchat)"
    />
    <van-cell
      title="标签"
      @click="onClick('tags', '标签', JSON.stringify(information.tags))"
    >
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
    <van-cell
      title="社交问题"
      :value="problemComputed"
      is-link
      @click="onClick('problem', '社交问题', information.problem)"
    />
    <van-cell
      title="自我介绍"
      :value="information.introduction"
      is-link
      @click="onClick('introduction', '自我介绍', information.introduction)"
    />
  </van-cell-group>
  
</template>

<script>
import axios from "@/myAxios";
import { showToast } from "vant";
export default {
  data() {
    return {
      information: {
        userAccount: null,
        username: null,
        avatarUrl: null,
        gender: null,
        age: null,
        address: null,
        phone: null,
        wchat: null,
        introduction: null,
        problem: null,
      },
    };
  },
  beforeDestroy(){
    console.log("dsdsd")
  },
  computed: {
    genderString() {
      if (this.information.gender == 1) {
        return "女";
      } else {
        return "男";
      }
    },
    problemComputed(){
      if(this.information.problem==="无"){
        return "未填写问题，联系方式默认直接被获取";
      }else{
        return this.information.problem
      }
    },
    wchatComputed(){
      if(this.information.wchat===null){
        return "未填写微信号，对方将无法获取联系方式"
      }else{
        return this.information.wchat
      }
    }
  },
  methods: {
    onClick(editKey, editTitle, editValue) {
      this.$router.push({
        path: "/edit",
        query: {
          editKey,
          editTitle,
          editValue,
        },
      });
    },
    quit() {
      localStorage.removeItem("token");
      this.$router.push("/login");
    },
    onClickLeft(){
      this.$router.push("/about");
    }
  },
  mounted() {
    axios.get("/user/information").then((res) => {
      if (res.code == 1) {
        this.information = res.data;
        this.information.tags = JSON.parse(this.information.tags);
      } else {
        showToast("错误：" + res.msg);
      }
    });
  },
};
</script>

<style>
</style>