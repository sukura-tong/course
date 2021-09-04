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
  name: "big-file",
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
      // 获取控件别名
      let file = _this.$refs.file.files[0];

      // 使用md5工具类生成文件唯一标识码
      let key = hex_md5(file.name + file.size + file.type);
      // let key10 = parseInt(key,16);
      // let key62 = Tool._10to62(key10);

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
      // 文件分片
      let shardSize = 20 * 1024 * 1024;//20MB为一个分片



      let shardIndex = 1;// 分片索引 1表示第一个分片
      let size = file.size;
      let shardTotal = Math.ceil(size / shardSize);

      /////****************** 第一种提交方式 : 表单提交 ********************/////
      // let formData = new window.FormData();
      // // key file必须和后端controller参数名一样
      // formData.append('shard',fileShard);
      // formData.append("shardIndex",shardIndex);
      // formData.append("shardSize",shardSize);
      // formData.append("shardTotal",shardTotal);
      // formData.append("use",_this.use);
      // formData.append("name",file.name);
      // formData.append("suffix",suffix);
      // formData.append("size",size);
      // formData.append("key",key);
      //
      // Loading.show();
      // _this.$ajax.post(process.env.VUE_APP_SERVER +  '/file/admin/file/bigfileupload', formData)
      //     .then((response) => {
      //       Loading.hide();
      //       let resp = response.data;
      //       console.log("文件上传成功", resp);
      //       // 清空控件操作、防止连续选择同一控件报错
      //       $("#" + _this.inputId + '-input').val("");
      //       _this.afterUpload(resp);
      //     });

      /////****************** 第二种提交方式 : js对象提交 ********************/////

      //监听到数据上传 设置提交参数
      let param = {
        'shardIndex': shardIndex,
        'shardSize': shardSize,
        'shardTotal': shardTotal,
        'use': _this.use,
        'name': file.name,
        'suffix': suffix,
        'size': file.size,
        'key': key
      };

      // 将图片转化为base64进行存储
      _this.check(param);

    },

    selectFile(){
      let _this = this;
      let selectId = _this.inputId + '-input';
      // console.log(selectId)
      $("#"+selectId).trigger("click");
    },

    upload: function (param) {
      let _this = this;
      let shardIndex = param.shardIndex;
      let shardTotal = param.shardTotal;
      let shardSize = param.shardSize;
      let fileShard = _this.getFileSizeShard(shardIndex,shardSize);

      let fileReader = new FileReader();
      // 事件监听绑定，当数据上传事件发生时，触发
      fileReader.onload = function (e) {
        let base64 = e.target.result;
        param.shard = base64;
        console.log("文件开始上传", param);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/file/oss-append', param).then((response) => {
              Loading.hide();
              let resp = response.data;
              console.log("文件分片上传成功",resp);
              if ((shardIndex < shardTotal)) {
                // 上传下一个分片
                param.shardIndex = param.shardIndex + 1;
                _this.upload(param);
              } else {
                console.log("文件分片上传成功",shardIndex);
                _this.afterUpload(response.data);
                // 清空控件操作、防止连续选择同一控件报错
                $("#" + _this.inputId + '-input').val("");
              }
            });
      };
      // 先开启监听 再进行数据读取
      fileReader.readAsDataURL(fileShard);
    },

    getFileSizeShard: function (shardIndex, shardSize) {
      let _this = this;
      let file = _this.$refs.file.files[0];
      let start = (shardIndex - 1) * shardSize;	//当前分片起始位置
      let end = Math.min(file.size, start + shardSize); //当前分片结束位置
      let fileShard = file.slice(start, end); //从文件中截取当前的分片数据
      return fileShard;
    },
    check(param){
      let _this = this;
      _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/admin/file/check' + param.key)
      .then((response) =>{
        let resp = response.data;
        if (resp.success){
          let obj = resp.content;
          if (!obj){
            param.shardIndex = 1;
            console.log("没有分片文件记录");
            _this.upload(param);
          }else if (obj.shardIndex == obj.shardTotal){
           Toast.success("文件极速秒传成功");
           _this.afterUpload(resp);
            $("#" + _this.inputId + '-input').val("");
          }else {
            param.shardIndex = obj.shardIndex + 1;
            console.log("找到分片文件记录");
            _this.upload(param);
          }
        }else {
          Toast.warning("文件上传失败");
          $("#" + _this.inputId + '-input').val("");
        }
      })
    },

  }
}
</script>