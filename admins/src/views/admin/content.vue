<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="red"> {{course.name}} </router-link>
    </h4>
    <hr>

    <file id="fload" v-bind:input-id="'content-file-upload'"
          v-bind:text="'上传文件'"
          v-bind:suffixs="['jpg', 'jpeg', 'png', 'mp4']"
          v-bind:use="FILE_USE.COURSE.key"
          v-bind:after-upload="afterUploadContentFile" ></file>
    <br>
    <table id="file-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>名称</th>
        <th>地址</th>
        <th>大小</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="(f, i) in files" v-bind:key="f.id">
        <td>{{f.name}}</td>
        <td>{{f.url}}</td>
        <td>{{f.size | formatFileSize}}</td>
        <td>
          <button v-on:click="deleteCourseFile(f)" class="btn btn-white btn-xs btn-warning btn-round">
            <i class="ace-icon fa fa-times red2"></i>
            删除
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    <form class="form-horizontal">
      <div class="form-group">
        <div class="col-lg-12">
          {{saveContentLabel}}
        </div>
      </div>
      <div class="form-group">
        <div class="col-lg-12">
          <div id="content"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-lg-12">
          {{saveContentLabel}}
        </div>
      </div>
    </form>
    <p>
        <router-link to="/business/course" type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
          <i class="ace-icon fa fa-arrow-left"></i>
          返回
        </router-link>
      <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveContent()">
        <i class="ace-icon fa fa-plus blue"></i>
        保存
      </button>&nbsp;
    </p>
  </div>
</template>

<script>
import File from "@/components/file";
export default {
  name: "business-course-content",
  components: {File},
  data:function (){
    return{
      course:{},
      FILE_USE: FILE_USE,
      saveContentLabel:"",
      files : [],
      saveContentInterval:{},
    }
  },
  mounted:function (){
    let _this = this;
    let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
    if (Tool.isEmpty(course)){
      _this.$router.push("/welcome");
    }
    _this.course = course;
    _this.init();
    // 激活样式
    _this.$parent.activeSidebar("business-course-sidebar");
  },
  destroyed:function () {
    let _this = this;
    console.log("组件销毁!")
    clearInterval(_this.saveContentInterval);
  },
  methods:{
    //  打开编辑框
    init() {
      let _this = this;
      let course = _this.course;
      let id = course.id;
      $("#content").summernote({
        focus: true,
        height: 300
      });

      // 先清空历史文本
      $("#content").summernote('code', '');
      _this.saveContentLabel = "";

      // 加载内容文件列表
      _this.listContentFiles();

      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/findcontentinfo/' + id).then((response)=>{
        Loading.hide();
        let resp = response.data;

        if (resp.success) {
          if (resp.content) {
            $("#content").summernote('code', resp.content.content);
          }

          // 定时自动保存
          _this.saveContentInterval = setInterval(function() {
            _this.saveContent();
          }, 50000);
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    listContentFiles(){
      let _this = this;
      let id = _this.course.id;
      _this.$ajax
          .get(process.env.VUE_APP_SERVER +  '/business/admin/course-content-file/querycourseinfobyid/'+ id)
          .then((response) => {
            let resp = response.data;
            if (resp.success){
              _this.files = resp.content;
            }
          });
    },
    deleteCourseFile(f){
      let _this = this;
      // 增加弹出框
      Loading.show();
      // 增加弹出框
      Confirm.show("删除文件后不可恢复确认删除!",function () {
        _this.$ajax
            .delete(process.env.VUE_APP_SERVER +  '/business/admin/course-content-file/deletecoursecontentfileinfo/'+ f.id)
            .then((response) => {
              Loading.show();
              if (response.data.success) {
                Toast.success("删除数据成功");
                Tool.removeObj(_this.files,f);
              } else {
                _this.list(1);
                Toast.error("删除数据失败");
              }
              Loading.hide();
            });
      })
    },
    saveContent(){
      let _this = this;
      let content = $("#content").summernote("code");
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/savecontentinfo', {
        id:_this.course.id,
        content:content
      }).then((response)=>{
        Loading.hide();
        if (response.data.success){
          let now = Tool.dateFormat("mm:ss");
          _this.saveContentLabel = "最后保存时间:" + now;
        }else {
          Toast.error("插入数据失败");
        }
      })
    },
    //富文本标记框文件回调方法
    afterUploadContentFile(resp){
      let _this = this;
      let meta = resp.content;
      let file = {};
      file.courseId = _this.course.id;
      file.url = meta.path;
      file.name = meta.name;
      file.size = meta.size;

      _this.$ajax
          .post(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/savecoursecontentfileinfo/',file)
          .then((response) => {
            let resp = response.data;
            if (resp.success){
              Toast.success("文件上传成功")
              _this.files.push(file);
            }
          });
    },
  }
}
</script>

<style scoped>

</style>