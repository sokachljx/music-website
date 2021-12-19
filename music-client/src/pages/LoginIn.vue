<template>
  <div class="signUp-page">
    <loginLogo />
    <div class="signUp">
      <div class="signUp-head">
        <span>账号登录</span>
      </div>
      <el-form
        :model="loginForm"
        status-icon
        :rules="rules"
        ref="loginForm"
        label-width="70px"
        class="demo-ruleForm"
      >
        <el-form-item prop="username" label="用户名">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input
            type="password"
            placeholder="密码"
            v-model="loginForm.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="code" label="验证码" >
          <el-input
            placeholder="验证码"
            v-model="loginForm.code"
            style="width:150px"
          ></el-input>
          <el-image :src="captchaImg" @click="getCaptcha" class="captchaImg" ></el-image>
        </el-form-item>
        <div class="login-btn">
          <el-button @click="goSignUp">注册</el-button>
          <el-button type="primary" @click="handleLoginIn">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import loginLogo from "../components/LoginLogo";
import { mixin } from "../mixins";
import { loginIn,code } from "../api/index";
export default {
  name: "login-in",
  mixins: [mixin],
  components: {
    loginLogo
  },
  data() {
    return {
      captchaImg:'',
      loginForm: {
        username: "",
        password: "",
        code:"",
        key:""
      },
      rules: {
        username: [
          { required: true, trigger: "blur", message: "请输入用户名" }
        ],
        password: [{ required: true, trigger: "blur", message: "请输入密码" }],
        code: [{ required: true, trigger: "blur", message: "请输入验证码" },
        {min:5,max:5,message:'长度为五个字符',trigger:'blur'}],
      }
    };
  },
  mounted() {
    this.changeIndex("登录");
    this.getCaptcha();
  },
  methods: {
    handleLoginIn() {
      let _this = this;
      let params = new URLSearchParams();
      console.log(this.loginForm.key);
      params.append("username", this.loginForm.username);
      params.append("password", this.loginForm.password);
      params.append("key",this.loginForm.key);
      params.append("code",this.loginForm.code);
      loginIn(params)
        .then(res => {
          if (res.code == 1) {
            _this.notify("登录成功", "success");
            _this.$store.commit("setLoginIn", true);
            _this.$store.commit("setUserId", res.userMsg.id);
            _this.$store.commit("setUsername", res.userMsg.username);
            _this.$store.commit("setAvator", res.userMsg.avator);
            setTimeout(function() {
              _this.$router.push({ path: "/" });
            }, 2000);
          } 
          else {
            _this.notify(res.msg, "error");
          }
        })
        .catch(err => {
          _this.notify("用户名或密码错误", "error");
        });
    },
    getCaptcha(){
      code()
      .then(res=>{
        // console.log(res)
        this.loginForm.key=res.data.key;
        this.captchaImg=res.data.captchaImg;
        this.loginForm.code='';
      })
    },
    goSignUp() {
      this.changeIndex("注册");
      this.$router.push({ path: "/sign-up" });
    },
    changeIndex(value) {
      this.$store.commit("setActiveName", value);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../assets/css/sign-up.scss";
</style>
