<template>
  <div>
    <h1>欢迎来到此页面</h1>
  </div>
</template>

<script>
export default {
  name: "welcome",
  mounted: function (){
    // $parent 代表调用父组件的方法
    // this.$parent.activeSidebar("welcome-sidebar");
  }
}
</script>
