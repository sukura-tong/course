<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

    <div class="row">
      <div v-for="teacher in teachers" class="col-md-3">
        <div>
          <span class="profile-picture">
            <img v-show="!teacher.image"
                 class="editable img-responsive editable-click editable-empty"
                 src="/static/image/讲师头像/头像1.jpg" v-bind:title="teacher.intro"/>

            <img v-show="teacher.image"
                 class="editable img-responsive editable-click editable-empty"
                 v-bind:src="teacher.image"
                 v-bind:title="teacher.intro"/>
          </span>

          <div class="space-4"></div>

          <div class="width-85 label label-info label-xlg arrowed-in arrowed-in-right">
            <div class="inline position-relative">
              <a href="javascript:;" class="user-title-label dropdown-toggle" data-toggle="dropdown">
                <i class="ace-icon fa fa-circle light-green"></i>
                &nbsp;
                <span class="white">{{teacher.position}}</span>
              </a>
            </div>
          </div>
        </div>

        <div class="space-6"></div>

        <div class="text-center">
          <a href="javascript:;" class="text-info bigger-110" v-bind:title="teacher.motto">
            <i class="ace-icon fa fa-user"></i>
            {{teacher.name}}【{{teacher.nickname}}】
          </a>
        </div>

        <div class="space-6"></div>

        <div class="profile-social-links align-center">
          <button v-on:click="edit(teacher)" class="btn btn-xs btn-info">
            <i class="ace-icon fa fa-pencil bigger-120"></i>
          </button>
          &nbsp;
          <button v-on:click="dele(teacher.id)" class="btn btn-xs btn-danger">
            <i class="ace-icon fa fa-trash-o bigger-120"></i>
          </button>
        </div>

        <div class="hr hr16 dotted"></div>

      </div>
    </div>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">姓名</label>
                <div class="col-sm-10">
                  <input v-model="teacher.name" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">昵称</label>
                <div class="col-sm-10">
                  <input v-model="teacher.nickname" class="form-control">
                </div>
              </div>

              <!--v-model 的作用是数据绑定-->

              <div class="form-group">
                <label class="col-sm-2 control-label">头像</label>
                <div class="col-sm-10">
<!--                  添加上传组件-->
                    <oss-big-file v-bind:text="'上传头像'"
                          v-bind:input-id="'image-upload'"
                          v-bind:suffixs="['jpg','png','jpeg']"
                          v-bind:use="FILE_USE.TEACHER.key"
                      v-bind:after-upload="afterUpload">
                    </oss-big-file>
                    <div v-show="teacher.image" class="red">
                      <div class="col-md-4">
                        <img v-bind:src="teacher.image" class="img-responsive">
                      </div>
                    </div>
                </div>
<!--                <button type="button"-->
<!--                        v-on:click="selectImage()"-->
<!--                        class="btn btn-white btn-default btn-round">-->
<!--                  <i class="ace-icon fa fa-upload"></i>-->
<!--                  上传头像-->
<!--                </button>-->
                <!--ref给控件追加一个别名-->
<!--                <div class="col-sm-10">-->
<!--                  <input class="hidden" type="file" v-on:change="uploadImage()" ref="file" id="file-upload-input">-->
<!--                  <div v-show="teacher.image" class="red">-->
<!--                    <div class="col-md-4">-->
<!--                      <img v-bind:src="teacher.image" class="img-responsive">-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </div>-->
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">职位</label>
                <div class="col-sm-10">
                  <input v-model="teacher.position" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">座右铭</label>
                <div class="col-sm-10">
                  <input v-model="teacher.motto" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">简介</label>
                <div class="col-sm-10">
                  <textarea v-model="teacher.intro" class="form-control" rows="5">
                  </textarea>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

  </div>
</template>

<script>
    // 导入pagination主键
    import Pagination from "../../components/pagination";
    import File from "@/components/file";
    import OssBigFile from "@/components/oss-big-file";
    import Vod from "@/components/vod";
    export default {
        name: "teacher",
        components: {Vod, OssBigFile, File, Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                teachers: [],
                // 绑定新增方法的数据
                teacher: {},
                FILE_USE: FILE_USE,
            }
        },
        // 页面初始化
        mounted: function (){
            let _this = this;
            _this.$refs.pagination.size = 5;
            _this.list(1);
        },
        // 控件绑定方法
        methods : {
            // 代表查询第几页
            list(page) {
                let _this = this;
                // 展开提示框 防止用户恶意点击
                Loading.show();
                // 请求提交
                // 使用post请求传递对象
                // 获取分页主键下拉框条数 $.refs根据主键别名获取值
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/queryteacherinfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询讲师表",response);
                        _this.teachers = response.data.content.list;
                         _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.teacher = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                        || Validator.require(_this.teacher.id,"id")
                        || Validator.require(_this.teacher.name,"姓名")
                        || Validator.length(_this.teacher.name,"姓名",1,50)
                        || Validator.length(_this.teacher.nickname,"昵称",1,50)
                        || Validator.length(_this.teacher.image,"头像",1,100)
                        || Validator.length(_this.teacher.position,"职位",1,50)
                        || Validator.length(_this.teacher.motto,"座右铭",1,50)
                        || Validator.length(_this.teacher.intro,"简介",1,500)
                )
                // 校验数据通过，发送给后端
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/saveteacherinfo',_this.teacher).then((response)=>{
                        Loading.hide();
                        console.log("保存数据",response);
                        if (response.data.success){
                            $("#form-modal").modal("hide");
                            Toast.success("插入数据成功");
                            _this.list(1);
                        }else {
                            $("#form-modal").modal("hide");
                            Toast.error("插入数据失败");
                            _this.list(1);
                        }
                    })
            },
            edit(teacher){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.teacher = $.extend({},teacher);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除讲师表后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/business/admin/teacher/deleteteacherinfo/'+ id)
                        .then((response) => {
                            Loading.show();
                            if (response.data.success) {
                                Toast.error("删除数据成功");
                                _this.list(1);
                            } else {
                                _this.list(1);
                                Toast.error("删除数据失败");
                            }
                            Loading.hide();
                        });
                });
            },
            afterUpload(resp){
              let _this = this;
              let image = resp.content.path;
              _this.teacher.image = image;
            }


        }
    }
</script>
