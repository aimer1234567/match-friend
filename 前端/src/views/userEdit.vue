<template>
  <div style="height: 100vh; position: relative">
    <van-nav-bar
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      title="个人信息编辑"
    >
    </van-nav-bar>

    <van-form @submit="onSubmit">
      <van-field
        v-if="inputShow"
        v-model="editUser.currentValue"
        :name="editUser.editKey"
        :label="editUser.editTitle"
        placeholder="请输入"
      />
      <van-cell-group inset>
        <van-field
          v-if="textShow"
          v-model="editUser.currentValue"
          rows="2"
          autosize
          :label="editUser.editTitle"
          type="textarea"
          maxlength="50"
          placeholder="不填写则默认为无"
          show-word-limit
        />
        <!-- 性别编辑 -->
        <van-radio-group v-if="switchShow" v-model="editUser.currentValue">
          <van-cell-group inset>
            <van-cell title="女" clickable @click="editUser.currentValue = '1'">
              <template #right-icon>
                <van-radio name="1" />
              </template>
            </van-cell>
            <van-cell title="男" clickable @click="editUser.currentValue = '0'">
              <template #right-icon>
                <van-radio name="0" />
              </template>
            </van-cell>
          </van-cell-group>
        </van-radio-group>
      </van-cell-group>
      <!-- 地址编辑 -->
      <div v-if="addressShow">
        <address-edit @addressValue="onAddressValue"></address-edit>
      </div>
      <!--出生日 -->
      <van-date-picker
        v-model="editUser.ageValue"
        v-if="ageShow"
        :show-toolbar="false"
        :min-date="minDate"
        :max-date="maxDate"
        :columns-type="['year']"
      />
      <!-- 头像 -->
      <nut-uploader v-if="avatarUrlShow" url="http://localhost:8080/user/avatarUrl" :headers="headers"></nut-uploader>
      <!-- <nut-uploader v-if="avatarUrlShow" url="http://175.178.74.112:8080/user/avatarUrl" :headers="headers"></nut-uploader> -->
      <!-- 提交 -->
      <div v-if="!avatarUrlShow" style="margin: 16px">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
    <!-- 标签 -->
    <search
      v-if="tagsShow"
      :exitChildTag="editUser.editValue"
      @tagsValue="onTagsValue"
      style="position: absolute; top: 35%"
    ></search>
  </div>
</template>
<script>
import myAxios from "@/myAxios";
import { showToast } from "vant";
import addressEdit from "@/components/addressEdit.vue";
import search from "@/components/search.vue";
export default {
  data() {
    return {
      switchShow: false, //性别编辑
      textShow: false, //文本编辑
      inputShow: false, //微信，电话号，用户名
      tagsShow: false, //标签
      addressShow: false, //地理位置
      ageShow: false,
      avatarUrlShow: false,
      minDate: new Date(1950, 1, 1),
      maxDate: new Date(),
      editUser: {
        ageValue: [],
        currentValue: null,
        editTitle: this.$route.query.editTitle,
        editKey: this.$route.query.editKey,
        editValue: this.$route.query.editValue,
      },
    };
  },
  computed:{
    headers(){
      return {
        token:localStorage.getItem('token')
      }
    }
  },
  components: {
    search,
    addressEdit,
  },
  created() {
    let key = this.$route.query.editKey;
    if (key == "username" || key == "phone" || key == "wchat") {
      this.inputShow = true;
      this.editUser.currentValue = this.editUser.editValue;
    } else if (key == "problem" || key == "introduction") {
      this.textShow = true;
      this.editUser.currentValue = this.editUser.editValue;
    } else if (key == "gender") {
      this.switchShow = true;
    } else if (key == "tags") {
      this.tagsShow = true;
      this.editUser.editValue=JSON.parse(this.editUser.editValue)
    } else if (key == "address") {
      this.addressShow = true;
    } else if (key == "age") {
      this.ageShow = true;
      let ageArray = [];
      if(this.editUser.editValue!=null){
         ageArray.push(this.editUser.editValue);
      }else{
        let now = new Date();
        ageArray.push(now);
      }
      this.editUser.ageValue = ageArray;
    } else {
      this.avatarUrlShow = true;
    }
  },
  methods: {
    // 提交个人信息修改的键值对
    onSubmit() {
      if(this.editUser.editKey=="age"){
        this.editUser.currentValue=this.editUser.ageValue[0]
        console.log(this.editUser.currentValue)
      }
      myAxios
        .post("/user/information/update", {
          key: this.editUser.editKey,
          value: this.editUser.currentValue,
        })
        .then((res) => {
          if (res.code == 1) {
            showToast("修改成功");
            history.back();
          } else {
            showToast("修改失败" + res.msg);
          }
        });
    },
    //点击返回
    onClickLeft: () => history.back(),
    //解析标签值
    onTagsValue(value) {
      console.log(value)
      this.editUser.currentValue = JSON.stringify(value);
    },
    onAddressValue(addressValue) {
      this.editUser.currentValue = addressValue;
    },
  },
};
</script>

<style>
</style>