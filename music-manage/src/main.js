import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import App from './App'
import router from './router/index'
import store from './store/index'
import '@/assets/js/iconfont.js'
import VCharts from 'v-charts'

Vue.use(VCharts)
Vue.use(ElementUI)

/* eslint-disable */
new Vue({
  el: '#app',
  router: router,
  store: store,
  render: h => h(App)
})
