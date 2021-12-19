<template>
  <div id="app">
    <the-header />
    <router-view class="music-content"/>
    <song-audio />
    <the-aside />
    <play-bar />
    <scroll-top />
    <the-footer />
  </div>
</template>

<script>
import ScrollTop from './components/ScrollTop.vue';
import TheHeader from './components/TheHeader';
import TheFooter from './components/TheFooter.vue';
import SongAudio from './components/SongAudio.vue';
import TheAside from './components/TheAside.vue';
import PlayBar from './components/PlayBar.vue';

export default {
  name: 'App',
  components:{
    TheHeader,
    ScrollTop,
    TheFooter,
    SongAudio,
    PlayBar,
    TheAside

  }
}
</script>
