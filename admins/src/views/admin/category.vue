<template>
    <div>
      <div class="row">
        <div class="col-md-6">
          <p>
            <button v-on:click="addOne()" class="btn btn-white btn-default btn-round">
              <i class="ace-icon fa fa-edit"></i>
              新增一级
            </button>
            &nbsp;
            <button v-on:click="list()" class="btn btn-white btn-default btn-round">
              <i class="ace-icon fa fa-refresh"></i>
              刷新
            </button>
          </p>
          <table id="levelOne-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
              <th>id</th>
              <th>名称</th>
              <th>顺序</th>
              <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="category in levelOne"
                v-on:click="onClickLevelOne(category)"
                v-bind:class="{'active' : category.id === active.id}">

              <td>{{category.id}}</td>
              <td>{{category.name}}</td>
              <td>{{category.sort}}</td>

              <td>
                <div class="hidden-sm hidden-xs btn-group">
                  <button v-on:click="edit(category)" class="btn btn-xs btn-info">
                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                  </button>
                  <button v-on:click="dele(category.id)" class="btn btn-xs btn-danger">
                    <i class="ace-icon fa fa-trash-o bigger-120"></i>
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <div class="col-md-6">
          <p>
            <button v-on:click="addTwo()" class="btn btn-white btn-default btn-round">
              <i class="ace-icon fa fa-edit"></i>
              新增二级
            </button>
          </p>
          <table id="levelTwo-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
              <th>id</th>
              <th>名称</th>
              <th>顺序</th>
              <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="category in levelTwo" >

              <td>{{category.id}}</td>
              <td>{{category.name}}</td>
              <td>{{category.sort}}</td>

              <td>
                <div class="hidden-sm hidden-xs btn-group">
                  <button v-on:click="edit(category)" class="btn btn-xs btn-info">
                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                  </button>
                  <button v-on:click="dele(category.id)" class="btn btn-xs btn-danger">
                    <i class="ace-icon fa fa-trash-o bigger-120"></i>
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

<!--分页组件-->
<!--        <pagination ref="pagination" v-bind:list = "list" v-bind:item-count="5">-->
<!--        </pagination>-->


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
                                            <label class="col-sm-2 control-label">父分类</label>
                                            <div class="col-sm-10">
                                              <p class="form-control-static">
                                                {{active.name || "无"}}
                                              </p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">名称</label>
                                            <div class="col-sm-10">
                                                <input v-model="category.name" type="text" class="form-control" >
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">顺序</label>
                                            <div class="col-sm-10">
                                                <input v-model="category.sort" type="text" class="form-control" >
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
    export default {
        name: "category",
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                categorys: [],
                // 绑定新增方法的数据
                category: {},
                levelOne:[],
                levelTwo:[],
                active:{},
            }
        },
        // 页面初始化
        mounted: function (){
            let _this = this;
            // _this.$refs.pagination.size = 5;
            _this.list(1);
        },
        // 控件绑定方法
        methods : {
            // 代表查询第几页
            list() {
                let _this = this;
                // 展开提示框 防止用户恶意点击
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/querycategoryinfo')
                    .then((response)=>{
                        Loading.hide();
                        // console.log("查询分类表",response);
                        _this.categorys = response.data.content.list;
                        // 将所有记录格式化为树形结构
                        // 将数据清空
                      _this.levelOne = [];
                      for (let i = 0; i <_this.categorys.length; i++){
                        let c = _this.categorys[i];
                        if (c.parent === '00000000'){
                          _this.levelOne.push(c);
                          for (let j = 0; j <_this.categorys.length; j++){
                            let child = _this.categorys[j];
                            if (child.parent === c.id){
                              if (Tool.isEmpty(c.children)){
                                c.children = [];
                              }
                              c.children.push(child);
                            }
                          }
                        }
                      }

                      _this.levelTwo = [];
                      //对当前一级分类表格触发点击事件 刷新二级菜单
                      setTimeout(function (){
                        $("tr.active").trigger("click");
                      },200)
                    })
            },
            // add的作用只是展开一个模态框
            // add(){
            //     let _this = this;
            //     _this.category = {};
            //     $("#form-modal").modal("show");
            // },
            save() {
                let _this = this;
                // 添加校验
                if (1 != 1
                        || Validator.require(_this.category.id,"id")
                        || Validator.require(_this.category.parent,"父id")
                        || Validator.require(_this.category.name,"名称")
                        || Validator.length(_this.category.name,"名称",1,50)
                )
                // 校验数据通过，发送给后端
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/savecategoryinfo',_this.category).then((response)=>{
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
            edit(category){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.category = $.extend({},category);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除分类表后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/business/admin/category/deletecategoryinfo/'+ id)
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
          onClickLevelOne(category){
            let _this = this;
            _this.active = category;
            _this.levelTwo = category.children;
          },

          addOne(){
              let _this = this;
              _this.active = {};
              _this.levelTwo = [];
              _this.category = {
                parent: "00000000"
              };
              $("#form-modal").modal("show");
          },
          addTwo(){
              let _this = this;
              if (Tool.isEmpty(_this.active)){
                Toast.warning("请先点击一级分类");
                return ;
              }
              let parentId = _this.active.id
              _this.category = {
                parent: parentId
              }
              $("#form-modal").modal("show");
          }
        }
    }
</script>

<style scoped>
  .active td{
    background-color: #d6e9c6 !important;
  }
</style>