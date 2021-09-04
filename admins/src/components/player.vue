<template>
  <div v-bind:id="playerId">

  </div>
</template>

<script>
export default {
  name: "player",
  props:{
    playerId:{
      default:"player-div",
    },
  },
  data:function (){
    return{
      // 播放器实例
      aliPlyer:{},
    }
  },
  methods:{
    playUrl(url){
      let _this = this;
      console.log("开始播放",url);

      if (_this.aliPlyer){
        _this.aliPlyer = null;
        $("#" + _this.playerId + '-player').remove();
      }

      // 添加播放器div
      // 初始化播放器
      $("#" + _this.playerId)
          .append("<div class=\"prism-player\" " +
              "id=\"" + _this.playerId + "-player\"></div>");
      _this.aliPlyer = new Aliplayer({
        id: _this.playerId + '-player',
        width: '100%',
        autoplay: false,
        //支持播放地址播放,此播放优先级最高
        source : url,
        cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
    },function (player){
      console.log("播放器创建完成");
    });
    },
    playVod(vod){
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/upload/getauth/' + vod)
            .then((response)=>{
              Loading.hide();
              let resp = response.data;
              if (resp.success){
                // 若存在播放器实例则清空
                if (_this.aliPlyer){
                  _this.aliPlyer = null;
                  $("#" + _this.playerId + '-player').remove();
                }

                // 初始化播放器
                $("#" + _this.playerId)
                    .append("<div class=\"prism-player\" " +
                        "id=\"" + _this.playerId + "-player\"></div>");

                _this.aliPlyer = new Aliplayer({
                  id: _this.playerId + '-player',
                  width: '100%',
                  autoplay: false,
                  //支持播放地址播放,此播放优先级最高
                  vid : vod,
                  playauth:resp.content,
                  cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
                  encryptType:1,
                },function (player){
                  console.log("播放器创建完成");
                });
              }
            })
    }
  },
}
</script>

<style scoped>

</style>