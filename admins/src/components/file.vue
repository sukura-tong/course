<template>
    <div>
      <button type="button"
              v-on:click="selectFile()"
              class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-upload"></i>
        {{text}}
      </button>
      <!--ref给控件追加一个别名-->
      <div class="col-sm-10">
        <input class="hidden" type="file" v-on:change="uploadFile()" ref="file" v-bind:id="inputId + '-input'">
      </div>
    </div>
</template>

<script>
export default {
  name: "file",
  //暴露的可配置属性
  props:{
    text:{
      default: "上传文件"
    },
    inputId:{
      default: "file-upload"
    },
    use:{
      default: ""
    },
    suffixs:{
      default: []
    },
    //回调函数
    afterUpload:{
      type: Function,
      default: null
    },
  },
  data:function () {
    return{

    }
  },
  methods:{
    uploadFile(){
      let _this = this;
      let formData = new window.FormData();
      // 获取控件别名
      let file = _this.$refs.file.files[0];
      // 上传文件类型判断
      let suffixs = _this.suffixs;
      let fileName = file.name;
      let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
      let validateSuffix = false;

      for (let i = 0; i< suffixs.length; i++){
        if (suffixs[i].toLowerCase() === suffix){
          validateSuffix = true;
        }
      }
      if (!validateSuffix){
        Toast.warning("请上传正确的文件格式:"+suffixs.join(","));
        $("#" + _this.inputId + '-input').val("");
        return ;
      }
      // key file必须和后端controller参数名一样
      formData.append('file',file);
      formData.append("use",_this.use);
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER +  '/file/admin/file/upload', formData)
          .then((response) => {
            Loading.hide();
            let resp = response.data;
            console.log("文件上传成功", resp);
            // 清空控件操作、防止连续选择同一控件报错
            $("#" + _this.inputId + '-input').val("");
            _this.afterUpload(resp);
          });
    },
    selectFile(){
      let _this = this;
      let selectId = _this.inputId + '-input';
      // console.log(selectId)
      $("#"+selectId).trigger("click");
    }

  }
}
</script>