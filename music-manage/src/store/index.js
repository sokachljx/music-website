import Vue from 'vue'
import Router from 'vue-router'
import Vuex from 'vuex'

Vue.use(Router)
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    HOST: 'http://127.0.0.1:3333',
    isPlay: false,    //是否播放中
    url: '',          //歌曲地址
    id: ''            //歌曲id
  },
  getters: {
    HOST: state => state.HOST,
    isPlay: state => state.isPlay,
    url: state => state.url,
    id: state => state.id
  },
  mutations: {
    setHOST: (state, HOST) => { state.HOST = HOST },
    setIsPlay: (state, isPlay) => { state.isPlay = isPlay },
    setUrl: (state, url) => { state.url = url },
    setId: (state, id) => { state.id = id }
  }

});
export default store

