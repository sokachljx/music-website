import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      component: reslove => require(['../pages/Login.vue'], reslove)
    },
    
    {
      path: '/Home',
      component: reslove => require(['../components/Home.vue'], reslove),
      children:[
        {
          path: '/Info',
          component: reslove => require(['../pages/InfoPage.vue'], reslove)
        },
        {
          path: '/Consumer',
          component: reslove => require(['../pages/ConsumerPage.vue'], reslove)
        },
        {
          path: '/Singer',
          component: reslove => require(['../pages/SingerPage.vue'], reslove)
        },
        {
          path: '/SongList',
          component: reslove => require(['../pages/SongListPage.vue'], reslove)
        },
        {
          path: '/Song',
          component: reslove => require(['../pages/SongPage.vue'], reslove)
        },
        {
          path: '/ListSong',
          component: reslove => require(['../pages/ListSongPage.vue'], reslove)
        },
        {
          path: '/Collect',
          component: reslove => require(['../pages/CollectPage.vue'], reslove)
        },
      ]
  }]
})
