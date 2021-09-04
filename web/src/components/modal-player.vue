<template>
  <div id="player-modal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-body">

          <player v-bind:player-id="'modal-player-div'"
                  ref="player"></player>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Player from "@/components/player";
export default {
  name: "modal-player",
  components: {Player},

  data:function (){
    return{
      aliPlyer:{},
    }
  },

  methods:{
    playerUrl(url){
      let _this = this;
      _this.$refs.player.playerUrl(url);
    },

    playVod(vod){
      let _this = this;
      _this.$refs.player.playVod(vod);
      $("#player-modal").modal("show");
    }
  }
}
</script>

<style scoped>
  #player-modal .modal-body{
    padding: 0;
  }
</style>