<template>
    <div>
      <h4 class="lighter">
        <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
        <router-link to="/business/course" class="pink"> {{course.name}} </router-link>：
        <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
        <router-link to="/business/chapter" class="pink"> {{chapter.name}} </router-link>
      </h4>
        <p>
          <router-link to="/business/chapter" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-arrow-left"></i>
            返回大章
          </router-link>
          &nbsp;
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

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>标题</th>
                <th>课程</th>
                <th>大章</th>
                <th>vod</th>
                <th>时长</th>
                <th>收费</th>
                <th>顺序</th>
                <th>操作</th>
<!--                <th>创建时间</th>-->
<!--                <th>修改时间</th>-->
            </tr>
            </thead>

            <tbody>
            <tr v-for="section in sections">

                    <td>{{section.id}}</td>
                    <td>{{section.title}}</td>
                    <td>{{section.courseId}}</td>
                    <td>{{section.chapterId}}</td>
                    <td>{{section.vod}}</td>
                    <td>{{section.time | formatSecond}}</td>
                    <td>{{SECTION_CHARGE | optionKV(section.charge)}}</td>
                    <td>{{section.sort}}</td>
<!--                    <td>{{section.createdAt}}</td>-->
<!--                    <td>{{section.updatedAt}}</td>-->

                <td>
                    <div class="hidden-sm hidden-xs btn-group">
                        <button v-on:click="play(section)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-camera bigger-120"></i>
                        </button>

                        <button v-on:click="edit(section)" class="btn btn-xs btn-info">
                          <i class="ace-icon fa fa-pencil bigger-120"></i>
                        </button>

                        <button v-on:click="dele(section.id)" class="btn btn-xs btn-danger">
                            <i class="ace-icon fa fa-trash-o bigger-120"></i>
                        </button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>

<!--      模态框-->
        <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">表单</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">

<!--                                <div class="form-group">-->
<!--                                    <label class="col-sm-2 control-label">ID</label>-->
<!--                                    <div class="col-sm-10">-->
<!--                                        <input v-model="section.id" type="text" class="form-control" >-->
<!--                                    </div>-->
<!--                                </div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标题</label>
                        <div class="col-sm-10">
                            <input v-model="section.title" type="text" class="form-control" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">课程</label>
                        <div class="col-sm-10">
<!--                                        <input v-model="section.courseId" type="text" class="form-control" >-->
                          <p class="form-control-static">
                            {{course.name}}
                          </p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">大章</label>
                        <div class="col-sm-10">
<!--                                        <input v-model="section.chapterId" type="text" class="form-control" >-->
                          <p class="form-control-static">
                            {{chapter.name}}
                          </p>
                        </div>
                    </div>

                        <div class="form-group">
                          <label class="col-sm-2 control-label">视频</label>
                          <div class="col-sm-10">
                            <!--                  添加上传组件-->
                            <vod v-bind:text="'上传视频'"
                                  v-bind:input-id="'video-upload'"
                                  v-bind:suffixs="['mp4']"
                                  v-bind:use="FILE_USE.COURSE.key"
                                  v-bind:after-upload="afterUpload">
                            </vod>

                            <div v-show="section.video" class="row">
                              <div class="col-md-9">
<!--                                // 追加player标签 ref 是别名-->
                                <player v-bind:player-id="'form-player-div'"
                                        ref="player"></player>
                                <video v-bind:src="section.video" id="video" controls="controls" hidden></video>
                              </div>
                            </div>
                          </div>
                        </div>


                    <div class="form-group">
                        <label class="col-sm-2 control-label">时长</label>
                        <div class="col-sm-10">
                            <input v-model="section.time" type="text" class="form-control" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">视频</label>
                        <div class="col-sm-10">
                            <input v-model="section.video" class="form-control" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                      <label class="col-sm-2 control-label">Vod</label>
                      <div class="col-sm-10">
                        <input v-model="section.vod" class="form-control" disabled>
                      </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">收费</label>
                        <div class="col-sm-10">
                            <select v-model="section.charge" class="form-control">
                              <option v-for="o in SECTION_CHARGE" v-bind:value="o.key">
                                {{o.value}}
                              </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">顺序</label>
                        <div class="col-sm-10">
                            <input v-model="section.sort" type="text" class="form-control" >
                        </div>
                    </div>
<!--                                <div class="form-group">-->
<!--                                    <label class="col-sm-2 control-label">创建时间</label>-->
<!--                                    <div class="col-sm-10">-->
<!--                                        <input v-model="section.createdAt" type="text" class="form-control" >-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                                <div class="form-group">-->
<!--                                    <label class="col-sm-2 control-label">修改时间</label>-->
<!--                                    <div class="col-sm-10">-->
<!--                                        <input v-model="section.updatedAt" type="text" class="form-control" >-->
<!--                                    </div>-->
<!--                                </div>-->
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
                    </div>
                </div><!-- /#form-modal-content -->
            </div><!-- /#form-modal-dialog -->
        </div><!-- /#form-modal -->

        <modal-player ref="modalPlayer"></modal-player>
    </div>
</template>

<script>
    // 导入pagination主键
    import Pagination from "../../components/pagination";
    import File from "@/components/file";
    import BigFile from "@/components/big-file";
    import OssBigFile from "@/components/oss-big-file";
    import Vod from "@/components/vod";
    import Player from "@/components/player";
    import ModalPlayer from "@/components/modal-player";
    export default {
        name: "section",
        components: {ModalPlayer, Player, Vod, OssBigFile, BigFile, Pagination,File},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                sections: [],
                // 绑定新增方法的数据
                section: {},
                chapter : {},
                FILE_USE:FILE_USE,
                course: {},
                SECTION_CHARGE: SECTION_CHARGE,
            }
        },
        // 页面初始化
        mounted: function (){
            let _this = this;
            _this.$refs.pagination.size = 5;
            // 获取缓存的大章数据
            let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) || {};
            // 空值校验
            if (Tool.isEmpty(chapter)){
              _this.$router.push("/welcome");
            }
            _this.chapter = chapter;

          // 获取缓存的大章数据
          let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
          // 空值校验
          if (Tool.isEmpty(course)){
            _this.$router.push("/welcome");
          }
          _this.course = course;
          _this.list(1);
          _this.$parent.activeSidebar("business-course-sidebar");
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
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/querysectioninfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                    chapterId : _this.chapter.id,
                    courseId: _this.course.id,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询小节",response);
                        _this.sections = response.data.content.list;
                        _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.section = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                // 校验数据通过，发送给后端
                _this.section.courseId = _this.course.id;
                _this.section.chapterId = _this.chapter.id;
                _this.section.video = "",
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/section/savesectioninfo',_this.section).then((response)=>{
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
            edit(section){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.section = $.extend({},section);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除小节后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/business/admin/section/delete/' + id )
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
          afterUpload(resp){
              let _this = this;
              console.log("video")
              let video = resp.content.path;
              let vod = resp.content.vod;
              _this.section.vod = vod;
              _this.section.video = video;
              _this.getTime();
              // 基于url进行播放
              _this.$refs.player.playUrl(video);
          },
          /**
           * 获取时长
           */
          getTime(){
              let _this = this;
              // 如果程序拿不到对应的值，增加延时操作
              setTimeout(function (){
                let ele = document.getElementById("video");
                _this.section.time = parseInt(ele.duration, 10);
              },3000);
          },
          play(section){
            let _this = this;
            _this.$refs.modalPlayer.playVod(section.vod);
          }
        }

    }
</script>

<style>
  video{
    width: 100%;
    height: auto;
    margin-top: 10px;
  }
</style>
