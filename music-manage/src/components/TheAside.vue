<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="#483D8B"
      text-color="#B0B3B2"
      
      active-text-color="#20a0ff"
      unique-opened
      router
    >
  
     <template v-for="item in items">
          <el-menu-item :index="item.index" :key="item.index" class="side">
            <template>
            <i :class="item.icon"></i>
            {{ item.title }}   
            </template>    
          </el-menu-item>
     </template>
    </el-menu>
  </div>
</template>

<script>
import bus from '../assets/js/bus'
export default {
  data() {
    return {
      collapse: false,
      items: [
        {
          icon: "el-icon-document",
          index: "Info",
          title: "系统首页"
        },
        {
          icon: "el-icon-document",
          index: "Consumer",
          title: "用户管理"
        },
        {
          icon: "el-icon-document",
          index: "SongList",
          title: "歌单管理"
        },
        {
          icon: "el-icon-document",
          index: "Singer",
          title: "歌手管理"
        }
      ]
    }
  },
  computed: {
      onRoutes(){
          return this.$route.path.replace('/','');
      }
  },
  created () {
      // 通过bus来进行组件间的通信，来折叠侧边栏
      bus.$on('collapse',msg=>{
          this.collapse = msg

      })
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  background-color: #483D8B;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}
 .sidebar::-webkit-scrollbar {
  width: 0;
}
.side{
  height:50px;
  margin-top: 50px;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
  cursor: pointer;
  
} 
.sidebar > ul {
  height: 100%;
}


</style>
