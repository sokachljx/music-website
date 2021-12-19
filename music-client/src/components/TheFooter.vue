<template>
  <div class="the-footer">
    <p v-for="(item, index) in footerList" :key="index">
      {{item}}
    </p>
  </div>
</template>

<script>
export default {
  name: 'the-footer',
  data () {
    return {
      footerList: [
        `关于 | 帮助 | 条款 | 反馈 | 联系我:2460540799@qq.com`,
        `Copyright © 2021 lu-jiaxin`
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-footer.scss';
</style>
