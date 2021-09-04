<template>
    <div>
        <p>
            <button v-show="hasResource('010101')"
                    v-on:click="add()" class="btn btn-white btn-default btn-round">
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
                <th>id</th>
                <th>登录名</th>
                <th>昵称</th>
                <th>密码</th>
                <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="user in users">

                        <td>{{user.id}}</td>
                        <td>{{user.loginName}}</td>
                        <td>{{user.name}}</td>
                        <td>{{user.password}}</td>

                <td>
                    <div class="hidden-sm hidden-xs btn-group">
                        <button v-show="hasResource('010103')"
                                v-on:click="edit(user)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-pencil bigger-120"></i>
                        </button>
                        <button v-show="hasResource('010101')"
                                v-on:click="editCode(user)" class="btn btn-xs btn-info">
                          <i class="ace-icon fa fa-key bigger-120"></i>
                        </button>
                        <button v-show="hasResource('010102')"
                                v-on:click="dele(user.id)" class="btn btn-xs btn-danger">
                            <i class="ace-icon fa fa-trash-o bigger-120"></i>
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

<!--                          <div class="form-group">-->
<!--                              <label class="col-sm-2 control-label">id</label>-->
<!--                              <div class="col-sm-10">-->
<!--                                  <input v-model="user.id" type="text" class="form-control" >-->
<!--                              </div>-->
<!--                          </div>-->
                          <div class="form-group">
                              <label class="col-sm-2 control-label">登录名</label>
                              <div class="col-sm-10">
<!--                                使用id是否有值区分增加和修改  v-bind有值就是disable -->
                                  <input v-model="user.loginName" v-bind:disabled="user.id" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">昵称</label>
                              <div class="col-sm-10">
                                  <input v-model="user.name" type="text" class="form-control" >
                              </div>
                          </div>
<!--                          使用v-show标签对密码框什么时候出现做判断-->
                          <div v-show="!user.id"
                              class="form-group" >
                              <label class="col-sm-2 control-label">密码</label>
                              <div class="col-sm-10">
                                  <input
                                      v-model="user.password" type="text" class="form-control" >
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

      <div id="edit-code-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">修改密码</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">

                <!--                          使用v-show标签对密码框什么时候出现做判断-->
                <div class="form-group" >
                  <label class="col-sm-2 control-label">新密码</label>
                  <div class="col-sm-10">
                    <input
                        v-model="user.password" type="password" class="form-control" >
                  </div>
                </div>

              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button v-on:click="savePassword()" type="button" class="btn btn-primary">保存</button>
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
        name: "user",
        components: {Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                users: [],
                // 绑定新增方法的数据
                user: {}
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
          hasResource(id){
            return Tool.hasResource(id);
          },
            // 代表查询第几页
            list(page) {
                let _this = this;
                // 展开提示框 防止用户恶意点击
                Loading.show();
                // 请求提交
                // 使用post请求传递对象
                // 获取分页主键下拉框条数 $.refs根据主键别名获取值
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/queryuserinfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询用户",response);
                        _this.users = response.data.content.list;
                         _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.user = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                        // || Validator.require(_this.user.id,"id")
                        || Validator.require(_this.user.loginName,"登录名")
                        || Validator.length(_this.user.loginName,"登录名",1,50)
                        || Validator.length(_this.user.name,"昵称",1,50)
                        || Validator.require(_this.user.password,"密码")
                )
                // 校验数据通过，发送给后端

                //使用md5加密密码
                _this.user.password = hex_md5(_this.user.password + KEY);
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/saveuserinfo',_this.user).then((response)=>{
                        Loading.hide();
                        console.log("保存数据",response);
                        if (response.data.success){
                            $("#form-modal").modal("hide");
                            Toast.success("插入数据成功");
                            _this.list(1);
                        }else {
                            $("#form-modal").modal("hide");
                            Toast.warning(response.data.message);
                            _this.list(1);
                        }
                    })
            },
            edit(user){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.user = $.extend({},user);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            editCode(user){
              let _this = this;
              _this.user = $.extend({},user);
              _this.user.password = null;
              $("#edit-code-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除用户后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/system/admin/user/deleteuserinfo/'+ id)
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
            savePassword(){
              let _this = this;
              //使用md5加密密码
              _this.user.password = hex_md5(_this.user.password + KEY);

              Loading.show();
              _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/savepassword',_this.user)
              .then((response)=>{
                Loading.hide();
                console.log("重置密码",response);
                if (response.data.success){
                  $("#edit-code-modal").modal("hide");
                  Toast.success("密码重置成功");
                  _this.list(1);
                }else {
                  $("#edit-code-modal").modal("hide");
                  Toast.warning(response.data.message);
                  _this.list(1);
                }
              })
            }
        }

    }
</script>
