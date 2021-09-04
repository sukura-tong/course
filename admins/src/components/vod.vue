<template>
<!--使用主键包含方式，复写oss-big-file文件-->
  <oss-big-file v-bind:input-id="inputId"
                v-bind:text="text"
                v-bind:suffixs="suffixs"
                v-bind:use="use"
                v-bind:after-upload="afterUpload"
                v-bind:shard-size="1000 * 1024 * 1024"
                v-bind:url="'vod-append'">

  </oss-big-file>
</template>

<script>
import OssBigFile from "@/components/oss-big-file";
export default {
  name: "vod",
  components: {OssBigFile},
  props:{
    text:{
      default:"上传vod",
    },
    inputId:{
      default: "vod-upload",
    },
    suffixs:{
      default: [],
    },
    use:{
      default: "",
    },
    afterUpload:{
      type:Function,
      default:null,
    },
  },
  methods:{}
}
</script>

<style scoped>

</style>