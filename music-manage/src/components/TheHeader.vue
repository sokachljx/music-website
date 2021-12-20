<template>
  <div class="header">
     <!-- 折叠按钮  -->
    <div class="collapse-btn" @click="collapseChange">
      <i class="el-icon-menu"></i>
    </div>
    <div class="logo">music后台管理</div>
    <div class="avator">
      <img src="../assets/img/acetor.jpg" />
    </div>
    <el-dropdown class="user-name" trigger="click" @command="handleCommand">
        <span class="el-dropdown-link">
            {{userName}}
            <i class="el-icon-caret-bottom"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import bus from "../assets/js/bus";
export default {
  data() {
    return {
      collapse: false,
      
      usernmae:'admin'
    };
  },
  computed:{
      userName(){
          return localStorage.getItem('userName');
      }
  },
  mounted () {
    if (document.body.clientWidth < 1500) {
      this.collapseChange()
    }
  },
  methods: {
      // 用户名下拉菜单选择事件
    handleCommand (command) {
      if (command === 'loginout') {
        this.$router.push('/')
      }
    },
    //侧边栏折叠
    collapseChange() {
      this.collapse = !this.collapse;
      bus.$emit("collapse", this.collapse);
    }
  }
};
</script>
<style scoped>
.header {
  position: absolute;
  z-index: 100;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #b0b3b2;
  background: #1a1b1c;
  box-shadow: 0px 0px 8px 2px rgba(0, 0, 0, 0.3);
}
.el-icon-menu{
    margin-top: 20px;
    margin-left: 150px;
    cursor: pointer;
}
.header .logo {
  width: 250px;
  margin-left: 200px;
  line-height: 70px;
  margin-top: -50px;
}
.avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  float: right;
  margin-top: -50px;
  margin-right: 80px;
  cursor: pointer;
}
.user-name {
  margin-right: 20px;
  float: right;
  margin-top: -40px;
}
.el-dropdown-link {
  color: #B0B3B2;
  cursor: pointer;
}
</style>
