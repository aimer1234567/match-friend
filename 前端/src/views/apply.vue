<template>
  <van-nav-bar title="申请" />
  <van-dropdown-menu>
    <van-dropdown-item
      v-model="applyKind"
      :options="option"
      @change="refreshFun"
    />
  </van-dropdown-menu>
  <nut-infinite-loading
    v-model="infinityValue"
    :has-more="hasMore"
    @load-more="loadMore"
    load-txt="加载中"
    style="height: 70vh;"
  >
  <nut-pull-refresh v-model="refresh" @refresh="refreshFun">
    <div :key="apply" v-for="apply in applys">
      <van-cell
        center
        size="large"
        :title="`用户名：${apply.username}`"
        :value="apply.isAgree"
      >
      </van-cell>
      <van-row gutter="10">
        <van-col>
          <van-button
            type="primary"
            @click="
              toIsApply(
                apply.applyId,
                apply.isAgree,
                apply.problem,
                apply.answer,
                apply.userAccount
              )
            "
            size="mini"
            >查看申请</van-button
          >
        </van-col>
        <van-col>
          <van-button
            type="primary"
            @click="toUserSpace(apply.userAccount)"
            size="mini"
            >查看用户主页</van-button
          >
        </van-col>
      </van-row>
    </div>
    </nut-pull-refresh>
  </nut-infinite-loading>

  <!-- 底部弹出 -->
  <van-popup
    v-model:show="showBottom"
    position="bottom"
    :style="{ height: '70%' }"
  >
    <template #default>
      <van-divider>问题</van-divider>
      {{ applySelected.problem }}
      <van-divider>回答</van-divider>
      {{ applySelected.answer }}
      <van-space
        direction="vertical"
        size="20px"
        style="width: 100%; position: absolute; bottom: 5%"
        fill
      >
        <van-button
          v-if="applySelected.isAgree === '未回复' && applyKind === 'applyFrom'"
          type="primary"
          block
          @click="onAgree(0)"
          >拒绝</van-button
        >
        <van-button
          v-if="applySelected.isAgree === '未回复' && applyKind === 'applyFrom'"
          type="primary"
          block
          @click="onAgree(1)"
          >同意</van-button
        >
        <van-button
          v-if="applySelected.isAgree === '已同意'"
          type="primary"
          block
          @click="toCommunication"
          >查看TA的联系方式</van-button
        >
      </van-space>
    </template>
  </van-popup>
</template>

<script>
import { showDialog } from "vant";
import { showToast } from "vant";
import myAxios from "@/myAxios";
import moment from "moment";
export default {
  data() {
    return {
      infinityValue: false,
      refresh:false,
      hasMore: true,
      time: null,
      pages: 0,
      pageSize: 10,
      applyKind: "applyTo",
      option: [
        { text: "我发出的申请", value: "applyTo" },
        { text: "我收到的申请", value: "applyFrom" },
      ],
      applys: [],
      applySelected: {
        problem: null,
        answer: null,
        applyId: null,
        isAgree: null,
        userAccount: null,
      },
      showBottom: false,
    };
  },
  mounted() {
    this.time = new Date();
    this.onSearch(this.applyKind);
  },
  methods: {
    //刷新
    refreshFun(){
      this.time = new Date();
      this.pages = 0;
      this.hasMore = true;
      this.onSearch(this.applyKind)
      this.refresh=false
    },
    toUserSpace(userAccount) {
      this.$router.push({
        name: "userSpace",
        params: { userAccount: userAccount },
      });
    },
    //分页加载
    loadMore() {
      setTimeout(() => {
        this.infinityValue = false;
        let newApplys = [];
        let path;
        this.pages = this.pages + 1;
        if (this.applyKind === "applyTo") {
          path = "message/query/applyTo";
        } else {
          path = "message/query/applyFrom";
        }
        myAxios
          .get(path, {
            params: {
              pages: this.pages,
              pageSize: this.pageSize,
              time: moment(this.time).format("YYYY-MM-DD HH:mm:ss"),
            },
          })
          .then((req) => {
            if (req.code === 1) {
              newApplys = req.data;
              //判断申请列表是否空
              if (newApplys.length >= 1) {
                for (let apply of newApplys) {
                  if (apply.isAgree === 0) {
                    apply.isAgree = "已拒绝";
                  } else if (apply.isAgree === 1) {
                    apply.isAgree = "已同意";
                  } else {
                    apply.isAgree = "未回复";
                  }
                }
                this.applys=this.applys.concat(newApplys);
              } else {
                this.hasMore = false;
              }
            } else {
              showToast(req.msg);
            }
          });
      }, 1000);
    },
    //分页第一次查询
    onSearch(applyKind) {
      let path;
      if (applyKind === "applyTo") {
        path = "message/query/applyTo";
      } else {
        path = "message/query/applyFrom";
      }
      myAxios
        .get(path, {
          params: {
            pages: this.pages,
            pageSize: this.pageSize,
            time: moment(this.time).format("YYYY-MM-DD HH:mm:ss"),
          },
        })
        .then((req) => {
          if (req.code === 1) {
            this.applys = req.data;
            //判断申请列表是否空
            if (this.applys.length >= 1) {
              for (let apply of this.applys) {
                if (apply.isAgree === 0) {
                  apply.isAgree = "已拒绝";
                } else if (apply.isAgree === 1) {
                  apply.isAgree = "已同意";
                } else {
                  apply.isAgree = "未回复";
                }
              }
            }
          } else {
            showToast(req.msg);
          }
        });
    },
    //更新要查看的申请的数据
    toIsApply(applyId, isAgree, problem, answer, userAccount) {
      this.showBottom = !this.showBottom;
      this.applySelected.problem = problem;
      this.applySelected.answer = answer;
      this.applySelected.applyId = applyId;
      this.applySelected.isAgree = isAgree;
      this.applySelected.userAccount = userAccount;
    },
    //同意或拒绝申请
    onAgree(is) {
      myAxios
        .get("/message/agree", {
          params: {
            is,
            applyId: this.applySelected.applyId,
          },
        })
        .then((res) => {
          if (res.code === 1) {
            showToast("操作成功");
          } else {
            showToast(res.msg);
          }
        });
      this.showBottom = !this.showBottom;
      this.refreshFun()
    },
    toCommunication() {
      myAxios
        .get("message/sendByApply", {
          params: {
            applyId: this.applySelected.applyId,
            userAccount: this.applySelected.userAccount,
          },
        })
        .then((res) => {
          if (res.code == 1) {
            showDialog({ message: `TA的微信号为：${res.data.wchat}` });
          } else {
            showToast("请求发送失败");
          }
        });
    },
  },
};
</script>

<style>
</style>