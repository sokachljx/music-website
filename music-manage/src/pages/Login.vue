<template>
  <div class="login-wrap">
    <div class="ms-title">网抑云MUSIC 后台管理</div>
    <div class="ms-login">
      <el-form
        ref="ruleForm"
        class="demo-ruleForm"
        :model="ruleForm"
        :rules="rules"
      >
        <el-form-item prop="username" >
          <el-input v-model="ruleForm.username"   placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            placeholder="密码"
            v-model="ruleForm.password" 
            @keyup.enter.native="submitForm('ruleForm')"
          ></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm">登录</el-button>
        </div>
        <!-- <p style="font-size:12px;line-height:30px;color:#999;">Tips : 用户名和密码要写数据库里的。</p> -->
      </el-form>
    </div>
  </div>
</template>
<script>
import {mixin} from '../mixins/index'
import {getLoginStatus} from '../api/index';
export default {
  mixins:[mixin],
  data: function () {
    return {
      ruleForm: {
        username: 'admin',
        password: '123'
      },
      
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    submitForm () {
      let params = new URLSearchParams()
      params.append('name', this.ruleForm.username)
      params.append('password', this.ruleForm.password)
      getLoginStatus(params)
        .then(res => {
          // console.log("进来了"+res.code)
          if (res.code == 1) {
            localStorage.setItem('userName',this.ruleForm.username)
            this.$router.push('/Info')
            alert('登录成功')
            this.notify('欢迎回来哟！', 'success')
          } else {
            alert('登录失败')
            this.notify('登录失败，请交钱！', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>
<style scoped>
.login-wrap {
  position: relative;
  background: url('../assets/img/bg03.jpg');
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
}
.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  font-weight: 600;
  color: #fff;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 180px;
  margin: -150px 0 0 -190px;
  padding: 40px;
  border-radius: 5px;
  background: #fff;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
  background-color: rgb(90, 154, 226);
}
.el-input__inner {
  width: 150px;
  height:30px;
  
}



</style>
