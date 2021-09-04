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

        <pagination ref="pagination" v-bind:list = "list" v-bind:item-count="5">
        </pagination>

        <div class="row">
          <div class="col-md-4"  v-for="course in courses">
            <div class="thumbnail search-thumbnail">
              <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
              <img v-show="course.image" class="media-object" v-bind:src="course.image" />

              <div class="caption">
                <div class="clearfix">
                  <span class="pull-right label label-danger info-label">
                    {{COURSE_LEVEL | optionKV(course.level)}}
                  </span>
                  <span class="pull-right label label-yellow info-label">
                    {{COURSE_CHARGE | optionKV(course.charge)}}
                  </span>
                  <span class="pull-right label label-primary info-label">
                    {{COURSE_STATUS | optionKV(course.status)}}
                  </span>
                </div>

                <!--使用css设定样式-->
                <h3 class="search-title">
                  <a href="#" class="blue">{{course.name}}</a>
                </h3>

                <!--讲师头像-->
                <div v-for="teacher in teachers.filter(t => {return t.id === course.teacherId})"
                class="profile-activity clearfix">
                    <div>
                      <img v-show="!teacher.image" class="pull-left" src="/ace/assets/images/avatars/avatar5.png">
                      <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">

                      <a class="user" href="#">
                        {{teacher.name}}
                      </a>
                      <br>
                      {{teacher.position}}

                    </div>

                </div>

                <p>
                  <span class="red bolder bigger-150">
                    {{course.price}}
                    &nbsp;
                    <i class="fa fa-rmb"></i>
                  </span>
                </p>
                <p>
                  <span class="badge badge-info">{{course.id}}</span> &nbsp;
                  <span class="badge badge-info">排序：{{course.sort}}</span>&nbsp;
                  <span class="badge badge-info">{{course.time | formatSecond}}</span>
                </p>

                <!--添加按钮-->
                <p>
                  <button v-on:click="edit(course)" class="btn btn-pink btn-xs btn-info btn-round">
<!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                    编辑
                  </button>
                  &nbsp;
<!--                  <button v-on:click="editContent(course)" class="btn btn-pink btn-xs btn-info btn-round">-->
                  <button v-on:click="toContent(course)" class="btn btn-pink btn-xs btn-info btn-round">
                    <!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                    内容
                  </button>
                  &nbsp; &nbsp;
                  <button v-on:click="toChapter(course)" class="btn btn-pink btn-xs btn-info btn-round">
                    <!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                    大章
                  </button>
                  &nbsp; &nbsp;
                  <button v-on:click="openSortModal(course)" class="btn btn-pink btn-xs btn-info btn-round">
                    <!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                    排序
                  </button>
                   &nbsp;
                  <button v-on:click="dele(course.id)" class="btn btn-yellow btn-xs btn-info btn-round">
                    <!--                    <i class="ace-icon fa fa-trash-o bigger-120"></i>-->
                    删除
                  </button>
                </p>
              </div>

            </div>
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
                            <label class="col-sm-2 control-label">分类</label>
                            <div class="col-sm-10">
                              <ul id="tree" class="ztree"></ul>
                            </div>
                          </div>

                          <div class="form-group">
                              <label class="col-sm-2 control-label">名称</label>
                              <div class="col-sm-10">
                                  <input v-model="course.name" type="text" class="form-control" >
                              </div>
                          </div>

                          <div class="form-group">
                            <label class="col-sm-2 control-label">封面</label>
                            <div class="col-sm-10">
                              <!--                  添加上传组件-->
                              <oss-big-file v-bind:text="'上传封面'"
                                    v-bind:input-id="'course-upload'"
                                    v-bind:suffixs="['jpg','png','jpeg']"
                                    v-bind:use="FILE_USE.COURSE.key"
                                    v-bind:after-upload="afterUpload">
                              </oss-big-file>
                              <div v-show="course.image" class="red">
                                <div class="col-md-7">
                                  <img v-bind:src="course.image" class="img-responsive">
                                </div>
                              </div>
                            </div>
                          </div>

                          <div class="form-group">
                              <label class="col-sm-2 control-label">概述</label>
                              <div class="col-sm-10">
                                  <input v-model="course.summary" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">时长</label>
                              <div class="col-sm-10">
                                  <input v-model="course.time" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">价格(元)</label>
                              <div class="col-sm-10">
                                  <input v-model="course.price" type="text" class="form-control" >
                              </div>
                          </div>



<!--                          <div class="form-group">-->
<!--                              <label class="col-sm-2 control-label">封面</label>-->
<!--                              <div class="col-sm-10">-->
<!--                                  <input v-model="course.image" type="text" class="form-control" >-->
<!--                              </div>-->
<!--                          </div>-->
                          <div class="form-group">
                              <label class="col-sm-2 control-label">级别</label>
                              <div class="col-sm-10">
                                 <select v-model="course.level" class="form-control">
                                   <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">
                                     {{o.value}}
                                   </option>
                                 </select>
                              </div>
                          </div>
                          <div class="form-group">
                            <label class="col-sm-2 control-label">讲师</label>
                            <div class="col-sm-10">
                              <select v-model="course.teacherId" class="form-control">
                                <option v-for="o in teachers" v-bind:value="o.id">
                                  {{o.name}}
                                </option>
                              </select>
                            </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">收费</label>
                              <div class="col-sm-10">
                                <select v-model="course.charge" class="form-control">
                                  <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">
                                    {{o.value}}
                                  </option>
                                </select>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">状态</label>
                              <div class="col-sm-10">
                                <select v-model="course.status" class="form-control">
                                  <option v-for="o in COURSE_STATUS" v-bind:value="o.key">
                                    {{o.value}}
                                  </option>
                                </select>
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">报名数</label>
                              <div class="col-sm-10">
                                  <input v-model="course.enroll" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">顺序</label>
                              <div class="col-sm-10">
                                  <input v-model="course.sort" type="text" class="form-control" disabled>
                              </div>
                          </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
                    </div>
                </div><!-- /#form-modal-content -->
            </div><!-- /#form-modal-dialog -->
        </div><!-- /#form-modal -->

<!--        <div id="course-content-modal" class="modal fade" tabindex="-1" role="dialog">-->
<!--        <div class="modal-dialog" role="document">-->
<!--          <div class="modal-content">-->
<!--            <div class="modal-header">-->
<!--              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
<!--              <h4 class="modal-title">课程内容编辑</h4>-->
<!--            </div>-->
<!--            <div class="modal-body">-->

<!--              <file v-bind:input-id="'content-file-upload'"-->
<!--                    v-bind:text="'上传文件'"-->
<!--                    v-bind:suffixs="['jpg','jpeg','mp4','png']"-->
<!--                    v-bind:use="FILE_USE.COURSE.key"-->
<!--                    v-bind:after-upload="afterUploadContentFile"></file>-->

<!--              <table id="file-table" class="table  table-bordered table-hover">-->
<!--                <thead>-->
<!--                <tr>-->
<!--                  <th>名称</th>-->
<!--                  <th>地址</th>-->
<!--                  <th>大小</th>-->
<!--                  <th>操作</th>-->
<!--                </tr>-->
<!--                </thead>-->

<!--                <tbody>-->
<!--                <tr v-for="(f,i) in files" v-bind:key="f.id">-->
<!--                  <td>{{f.name}}</td>-->
<!--                  <td>{{f.url}}</td>-->
<!--                  <td>{{f.size}}</td>-->
<!--                  <td>-->
<!--                    <div class="hidden-sm hidden-xs btn-group">-->
<!--                      <button v-on:click="deleteCourseFile(f)" class="btn btn-yellow btn-xs btn-info btn-round">-->
<!--                        &lt;!&ndash;                    <i class="ace-icon fa fa-trash-o bigger-120"></i>&ndash;&gt;-->
<!--                        删除-->
<!--                      </button>-->
<!--                    </div>-->
<!--                  </td>-->
<!--                </tr>-->
<!--                </tbody>-->
<!--              </table>-->


<!--              <form class="form-horizontal">-->
<!--                <div class="form-group">-->
<!--                  <div class="col-lg-12">-->
<!--                    {{saveContentLabel}}-->
<!--                  </div>-->
<!--                </div>-->

<!--                <div class="form-group">-->
<!--                  <div class="col-lg-12">-->
<!--                  &lt;!&ndash; 该区域追加副文本框&ndash;&gt;-->
<!--                    <div id="content"></div>-->
<!--                  </div>-->
<!--                </div>-->

<!--              </form>-->
<!--            </div>-->
<!--            <div class="modal-footer">-->
<!--              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>-->
<!--              <button v-on:click="saveContent()" type="button" class="btn btn-primary">保存</button>-->
<!--            </div>-->
<!--          </div>&lt;!&ndash; /#form-modal-content &ndash;&gt;-->
<!--        </div>&lt;!&ndash; /#form-modal-dialog &ndash;&gt;-->
<!--        </div>&lt;!&ndash; /#form-modal &ndash;&gt;-->

        <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog" style="overflow: auto">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">课程排序</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">

                <div class="form-group">
                  <label class="col-sm-2 control-label">当前顺序</label>
                  <div class="col-lg-9">
                    <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled=""/>
                  </div>
                </div>

                <div class="form-group">
                  <label class="col-sm-2 control-label">新顺序</label>
                  <div class="col-lg-9">
                    <input class="form-control" v-model="sort.newSort" name="newSort"/>
                  </div>
                </div>

              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button v-on:click="saveSortInfo()" type="button" class="btn btn-primary">保存</button>
            </div>
          </div><!-- /#form-modal-content -->
        </div><!-- /#form-modal-dialog -->
      </div><!-- /#form-modal -->
    </div>
</template>

<script>
    // 导入pagination主键
    import Pagination from "../../components/pagination";
    import category from "@/views/admin/category";
    import File from "@/components/file";
    import Vod from "@/components/vod";
    import OssBigFile from "@/components/oss-big-file";
    export default {
        name: "course",
        components: {OssBigFile, Vod, File, Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                courses: [],
                // 绑定新增方法的数据
                course: {},
                categorys : [],
                tree:{},
                COURSE_LEVEL: COURSE_LEVEL,
                COURSE_CHARGE: COURSE_CHARGE,
                COURSE_STATUS: COURSE_STATUS,
                FILE_USE: FILE_USE,
                saveContentLabel:"",
                sort : {
                  id : "",
                  oldSort : 0,
                  newSort : 0
                },
                teachers:[],
                files : [],
            }
        },
        // 页面初始化
        mounted: function (){
            let _this = this;
            _this.$refs.pagination.size = 5;
            _this.listAllCategory();
            _this.listAllTeacher();
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
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/querycourseinfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询课程表",response);
                  _this.courses = response.data.content.list;
                  _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.course = {
                  //设置顺序变量
                  sort : _this.$refs.pagination.total + 1
                };
                // 设置所有节点处于不选中状态
                _this.tree.checkAllNodes(false);
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                        || !Validator.require(_this.course.name,"名称")
                        // || !Validator.length(_this.course.name,"名称",1,50)
                        // || !Validator.length(_this.course.summary,"概述",1,2000)
                        || !Validator.require(_this.course.price,"价格(元)")
                        || !Validator.require(_this.course.level,"级别")
                ){
                  Toast.warning("校验出错");
                  $.router.push("/business/course");
                }
                // 校验数据通过，发送给后端
                // 获取树选择的节点
                let categorys = _this.tree.getCheckedNodes();
                if (Tool.isEmpty(categorys)){
                  Toast.warning("请选择分类");
                  return ;
                }
                _this.course.categorys = categorys;
                // console.log(categorys);
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/savecourseinfo',_this.course).then((response)=>{
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
            // 保存排序模态框
            saveSortInfo(){
              let _this = this;
              if (_this.sort.newSort === _this.sort.oldSort){
                Toast.warning("顺序未发生变化!");
                return ;
              }
              // console.log(_this.sort.id,_this.sort.oldSort,_this.sort.newSort)
              Loading.show();
              _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/savesortinfo/',{
                id:_this.sort.id,
                oldSort: _this.sort.oldSort,
                newSort:_this.sort.newSort
              }).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    if (resp.success){
                      Toast.success("更新成功");
                      $("#course-sort-modal").modal("hide");
                      _this.list(1);
                    }else {
                      Toast.error("更新失败");
                    }
                  })
            },

            listCategoryById(courseId){
              let _this = this;
              // 展开提示框 防止用户恶意点击
              Loading.show();
              _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/listcategoryInfo/' + courseId)
                  .then((response)=>{
                    Loading.hide();
                    // console.log("查询分类表",response);
                    let categoryInfos = response.data.content;
                    //勾选查询到的分类信息
                    _this.tree.checkAllNodes(false);

                    for (let i = 0; i < categoryInfos.length; i++){
                      let node = _this.tree.getNodeByParam("id",categoryInfos[i].categoryId);
                      _this.tree.checkNode(node, true);
                    }
                  })
            },
            edit(course){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.course = $.extend({},course);
                // 选中对应节点
                _this.listCategoryById(course.id);

                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除课程表后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/business/admin/course/deletecourseinfo/'+ id)
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
                })
            },
          /**
           * 大章
           * @param course
           */
          toChapter(course){
            let _this = this;
            // 缓存course数据
            SessionStorage.set(SESSION_KEY_COURSE,course);
            // 页面跳转
            _this.$router.push("/business/chapter");
          },

          toContent(course){
            let _this = this;
            SessionStorage.set(SESSION_KEY_COURSE,course);
            _this.$router.push("/business/content");
          },

          listAllCategory() {
            let _this = this;
            // 展开提示框 防止用户恶意点击
            Loading.show();
            _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/querycategoryinfo')
                .then((response)=>{
                  Loading.hide();
                  // console.log("查询分类表",response);
                  _this.categorys = response.data.content.list;
                  _this.initTree();
                })
          },
          listAllTeacher() {
            let _this = this;
            // 展开提示框 防止用户恶意点击
            Loading.show();
            _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/teacher/queryallteacher')
                .then((response)=>{
                  Loading.hide();
                  // console.log("查询分类表",response);
                  _this.teachers = response.data.content;
                  // _this.initTree();
                })
          },
          /**
           * 初始化树
           */
          initTree(){
            let _this = this;
            let setting = {
              check: {
                enable: true
              },
              data: {
                simpleData: {
                  idKey: "id",
                  pIdKey: "parent",
                  rootPId: "00000000",
                  enable: true
                }
              }
            };
            let zNodes = _this.categorys;

            _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
            // 展开节点
            _this.tree.expandAll(true);
          },
          /**
           * 保存文本框内容
           * */
          // saveContent(){
          //   let _this = this;
          //   let content = $("#content").summernote("code");
          //   Loading.show();
          //   _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/savecontentinfo', {
          //     id:_this.course.id,
          //     content:content
          //   }).then((response)=>{
          //     Loading.hide();
          //     if (response.data.success){
          //       let now = Tool.dateFormat("mm:ss");
          //       _this.saveContentLabel = "最后保存时间:" + now;
          //     }else {
          //       Toast.error("插入数据失败");
          //     }
          //   })
          // },

          // 排序方法
          openSortModal(course){
            let _this = this;
            // _this.course = course;
            // console.log(course.id);
            _this.sort = {
              id : course.id,
              oldSort: course.sort,
              newSort: course.sort
            };
            $("#course-sort-modal").modal("show");
          },
          /**
           * 编辑文本框
           * @param course
           */
          // editContent(course){
          //   let _this = this;
          //   _this.course = course;
          //   let id = course.id;
          //   //初始化副文本框 根据id选择器
          //   $("#content").summernote({
          //     focus:true,
          //     height:300
          //   })
          //   // 清空历史信息
          //   _this.saveContentLabel = "";
          //   $("#content").summernote('code','');
          //
          //   // 加载课程文件列表
          //   _this.listContentFiles();
          //
          //   Loading.show();
          //   _this.$ajax.post(process.env.VUE_APP_SERVER +  '/business/admin/course/findcontentinfo/'+ id)
          //       .then((response) => {
          //         Loading.hide();
          //         let resp = response.data;
          //         if (resp.success){
          //           //设置文本框所属模态框静态
          //           $("#course-content-modal").modal({backdrop:'static',keyboard:false});
          //           if (resp.content){
          //             $("#content").summernote('code',resp.content.content);
          //           }
          //           // 追加定时保存数据任务
          //           let saveContentInterVal = setInterval(function (){
          //             _this.saveContent();
          //           },50000);
          //           // 关闭内容框，清空自动保存任务
          //           $('#course-content-modal').on('hidden.bs.modal',function (e){
          //             clearInterval(saveContentInterVal);
          //           })
          //         }else {
          //           Toast.warning(resp.message);
          //         }
          //       });
          // },

          afterUpload(resp){
            let _this = this;
            _this.course.image = resp.content.path;
          },

          //富文本标记框文件回调方法
          // afterUploadContentFile(resp){
          //   let _this = this;
          //   let meta = resp.content;
          //   let file = {};
          //   file.courseId = _this.course.id;
          //   file.url = meta.path;
          //   file.name = meta.name;
          //   file.size = meta.size;
          //
          //   _this.$ajax
          //       .post(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/savecoursecontentfileinfo/',file)
          //       .then((response) => {
          //         let resp = response.data;
          //         if (resp.success){
          //           Toast.success("文件上传成功")
          //           _this.files.push(file);
          //         }
          //       });
          // },

          // listContentFiles(){
          //   let _this = this;
          //   let id = _this.course.id;
          //   _this.$ajax
          //       .get(process.env.VUE_APP_SERVER +  '/business/admin/course-content-file/querycourseinfobyid/'+ id)
          //       .then((response) => {
          //         let resp = response.data;
          //         if (resp.success){
          //           _this.files = resp.content;
          //         }
          //       });
          // },

          // deleteCourseFile(f){
          //   let _this = this;
          //   // 增加弹出框
          //   Loading.show();
          //   // 增加弹出框
          //   Confirm.show("删除文件后不可恢复确认删除!",function () {
          //     _this.$ajax
          //         .delete(process.env.VUE_APP_SERVER +  '/business/admin/course-content-file/deletecoursecontentfileinfo/'+ f.id)
          //         .then((response) => {
          //           Loading.show();
          //           if (response.data.success) {
          //             Toast.success("删除数据成功");
          //            Tool.removeObj(_this.files,f);
          //           } else {
          //             _this.list(1);
          //             Toast.error("删除数据失败");
          //           }
          //           Loading.hide();
          //         });
          //   })
          // }

        }

    }
</script>

<style scoped>
  .caption h3{
    font-size: 20px;
  }
</style>