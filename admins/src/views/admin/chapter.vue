<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink"> {{course.name}} </router-link>：
<!--      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>-->
<!--      <router-link to="/business/chapter" class="pink"> {{chapter.name}} </router-link>-->
    </h4>
    <p>
      <router-link to="/business/course" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-arrow-left"></i>
        返回课程
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
            <th>课程ID</th>
            <th>名称</th>
            <th>操作</th>

          </tr>
          </thead>

          <tbody>
          <tr v-for="chapter in chapters">
            <td>{{chapter.id}}</td>
            <td>{{chapter.courseId}}</td>
            <td>{{chapter.name}}</td>
            <td>
              <div class="hidden-sm hidden-xs btn-group">
<!--                <button v-on:click="edit(chapter)" class="btn btn-xs btn-info">-->
<!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
<!--                </button>-->
<!--                <button v-on:click="dele(chapter)" class="btn btn-xs btn-danger">-->
<!--                  <i class="ace-icon fa fa-trash-o bigger-120"></i>-->
<!--                </button>-->
                <button v-on:click="edit(chapter)" class="btn btn-pink btn-xs btn-info btn-round">
                  <!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                  编辑
                </button>
                <button v-on:click="dele(chapter.id)" class="btn btn-yellow btn-xs btn-info btn-round">
                  <!--                    <i class="ace-icon fa fa-trash-o bigger-120"></i>-->
                  删除
                </button>

                <button v-on:click="toSection(chapter)" class="btn btn-pink btn-xs btn-info btn-round">
                  <!--                  <i class="ace-icon fa fa-pencil bigger-120"></i>-->
                  小节
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>

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
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="chapter.name" type="text" class="form-control"  placeholder="名称">
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">
                    {{course.name}}
                  </p>
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

  </div>
</template>

<script>
// 导入pagination主键
import Pagination from "../../components/pagination";
export default {
  name: "chapter",
  components: {Pagination},

  // 定义方法的数据绑定
  data: function (){
    return{
      // 绑定查询方法的返回数据
      chapters: [],
      // 绑定新增方法的数据
      chapter: {},
      course: {},
    }
  },

  // 页面初始化
  mounted: function (){
    // sidebar 样式激活方法一
    // this.$parent.activeSidebar("business-chapter-sidebar");
    let _this = this;
    // 设置初始分页多少条
    _this.$refs.pagination.size = 5;
    let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
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
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/chapter/querychapterinfo',{
        // 使用post请求传递对象
        page: page,
        // 获取分页主键下拉框条数 $.refs根据主键别名获取值
        size: _this.$refs.pagination.size,
        courseId : _this.course.id,
      })
        .then((response)=>{
          Loading.hide();
          // console.log("查询大章",response);
          _this.chapters = response.data.chapterInfo;
          _this.$refs.pagination.render(page,response.data.pageDtoInfos.total);
      })
    },

    add(){
      let _this = this;
      _this.chapter = {};
      $("#form-modal").modal("show");
    },

    save(page) {
      let _this = this;

      // 保存校验

      if (!Validator.require(_this.chapter.name, "名称")
          || !Validator.length(_this.chapter.courseId, "课程ID", 1, 8)){
        return ;
      }
      _this.chapter.courseId = _this.course.id;
      // 校验数据通过，发送给后端
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/chapter/savechapterinfo',_this.chapter)
          .then((response)=>{
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

    edit(chapter){
      let _this = this;
      // 绑定数据字段 点击哪一行 获取哪一行的数据
      // 复制对象
      _this.chapter = $.extend({},chapter);
      // 展示编辑框
      $("#form-modal").modal("show");

    },

    dele(chapter){
      let _this = this;

      // 增加弹出框
      Confirm.show("删除大章后不可恢复确认删除!",function () {
        _this.$ajax
            .post(process.env.VUE_APP_SERVER +  '/business/admin/chapter/deletechapterinfo', chapter)
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

    toSection(chapter){
      let _this = this;
      SessionStorage.set(SESSION_KEY_CHAPTER,chapter);
      _this.$router.push("/business/section");
    }
  }

}
</script>
