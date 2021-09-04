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

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
                <th>id</th>
                <th>角色</th>
                <th>描述</th>
                <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="role in roles">

                        <td>{{role.id}}</td>
                        <td>{{role.name}}</td>
                        <td>{{role.desc}}</td>

        <td>
            <div class="hidden-sm hidden-xs btn-group">

                <button v-on:click="editUsers(role)" class="btn btn-xs btn-info">
                  <i class="ace-icon fa fa-user bigger-120"></i>
                </button>

                <button v-on:click="editResources(role)" class="btn btn-xs btn-info">
                  <i class="ace-icon fa fa-list bigger-120"></i>
                </button>

                <button v-on:click="edit(role)" class="btn btn-xs btn-info">
                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                </button>

                <button v-on:click="dele(role.id)" class="btn btn-xs btn-danger">
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

                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">id</label>
                                            <div class="col-sm-10">
                                                <input v-model="role.id" type="text" class="form-control" >
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">角色</label>
                                            <div class="col-sm-10">
                                                <input v-model="role.name" type="text" class="form-control" >
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">描述</label>
                                            <div class="col-sm-10">
                                                <input v-model="role.desc" type="text" class="form-control" >
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

      <div id="resources-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">角色资源关联配置</h4>
            </div>
            <div class="modal-body">
              <ul id="tree" class="ztree"></ul>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button v-on:click="saveRoleResources()" type="button" class="btn btn-primary">保存</button>
            </div>
          </div><!-- /#form-modal-content -->
        </div><!-- /#form-modal-dialog -->
      </div><!-- /#form-modal -->

      <div id="user-edit-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">角色用户关联配置</h4>
            </div>

<!--            左右两部分构成-->

            <div class="modal-body">
              <div class="row">
                <div class="col-md-6">
                  <table id="user-table" class="table table hover">
                    <tbody>
                    <tr v-for="user in users">
                      <td>{{user.loginName}}</td>
                      <td class="text-right">
                        <a v-on:click="addUser(user)" href="javascript:;" class="">
                          <i class="ace-icon fa fa-arrow-circle-right blue"></i>
                        </a>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>

                <div class="col-md-6">
                  <table id="role-user-table" class="table table hover">
                    <tbody>
                    <tr v-for="user in roleUsers">
                      <td>{{user.loginName}}</td>
                      <td class="text-right">
                        <a v-on:click="deleteUser(user)" href="javascrpit:;" class="">
                          <i class="ace-icon fa fa-trash blue"></i>
                        </a>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>


            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
              <button v-on:click="saveRoleUsers()" type="button" class="btn btn-primary">保存</button>
            </div>
          </div><!-- /#form-modal-content -->
        </div><!-- /#form-modal-dialog -->
      </div><!-- /#form-modal -->

    </div>
</template>

<script>
    // 导入pagination主键
    import Pagination from "../../components/pagination";
    import user from "@/views/admin/user";
    export default {
        name: "role",
        components: {Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
              // 绑定查询方法的返回数据
              roles: [],
              // 绑定新增方法的数据
              role: {},
              resources:[],
              tree:{},
              users:[],
              roleUsers:[],
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
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/queryroleinfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询角色",response);
                        _this.roles = response.data.content.list;
                         _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.role = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                        || Validator.require(_this.role.id,"id")
                        || Validator.require(_this.role.name,"角色")
                        || Validator.length(_this.role.name,"角色",1,50)
                        || Validator.require(_this.role.desc,"描述")
                        || Validator.length(_this.role.desc,"描述",1,100)
                )
                // 校验数据通过，发送给后端
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/saveroleinfo',_this.role).then((response)=>{
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
            edit(role){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.role = $.extend({},role);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除角色后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/system/admin/role/deleteroleinfo/'+ id)
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
          /*
          * 资源树保存
          * */
          saveRoleResources(){
            let _this = this;
            let resources = _this.tree.getCheckedNodes();
            console.log("勾选的资源为:",resources)

            // 保存时 只保存资源ID
            let resourceIds = [];
            for (let i = 0; i < resources.length; i++){
              resourceIds.push(resources[i].id);
            }

            console.log("post request params");
            console.log(_this.role.id);
            console.log(resourceIds);

            _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/savecheckresourcesid',
                {
                  roleId:_this.role.id,
                  resourceIds:resourceIds
                })
            .then((resopnse) =>{
                let resp = resopnse.data;
                if (resp.success){
                  Toast.success("保存成功");
                }else {
                  Toast.error(resp.message);
                }
            });


          },
          editResources(role){
            let _this = this;
            // 绑定数据字段 点击哪一行 获取哪一行的数据
            // 复制对象
            _this.role = $.extend({},role);
            // 查找数据
            _this.listResources();
            // 展示编辑框
            $("#resources-modal").modal("show");
          },
          /**
           * 加载资源树
           */
          listResources(){
            let _this = this;
            // 展开提示框 防止用户恶意点击
            Loading.show();
            // 请求提交
            // 使用post请求传递对象
            // 获取分页主键下拉框条数 $.refs根据主键别名获取值
            _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/resources/loadtree')
                .then((response)=>{
                  Loading.hide();
                  // console.log("查询资源",response);
                  let resp = response.data;
                  _this.resources = resp.content;
                  //初始化树
                  _this.initTree();
                  // 初始化选中情况
                  _this.listRoleResource();
                })
          },
          /**
           * 资源树初始化
           */
          initTree(){
            let _this = this;
            let setting = {
              check:{
                enable:true
              },
              data:{
                simpleData: {
                  idKey: "id",
                  pIdKey: "parent",
                  rootPId: "",
                  enable: true
                }
              }
            };
            let zNodes = _this.resources;
            _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
            // 展开节点
            _this.tree.expandAll(true);

          },
          /**
           * 加载角色关联记录表
           * */
          listRoleResource(){
            let _this = this;
            _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/role/loadroleresources/'+_this.role.id)
                .then((response)=>{
                Loading.hide();
                // console.log("查询资源",response);
                let resp = response.data;
                let resources = resp.content;
                console.log("加载角色关联记录的选中情况...")
                console.log(resources);
                // 勾选查询到的资源
                    // 1 清空所有树节点的勾选状态
                    _this.tree.checkAllNodes(false);
                    // 2 勾选查询到的资源
                    for (let i = 0; i < resources.length; i++){
                      let resource = resources[i];
                      let node = _this.tree.getNodeByParam("id",resource);
                      _this.tree.checkNode(node,true);
                    }
                })
          },
          editUsers(role){
              let _this = this;
              _this.role = $.extend({},role);
              // 查找数据
              _this.listAllUser();
              // 展示编辑框
              $("#user-edit-modal").modal("show");
          },
          listAllUser(){
            let _this = this;
            // 展开提示框 防止用户恶意点击
            Loading.show();
            // 请求提交
            // 使用post请求传递对象
            // 获取分页主键下拉框条数 $.refs根据主键别名获取值
            _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/queryuserinfo',{
              page: 1,
              size: 9999,
            }).then((response)=>{
              Loading.hide();
              // console.log("查询用户",response);
              _this.users = response.data.content.list;
              _this.$refs.pagination.render(1,response.data.content.total);
              _this.listRoleUser();
            })
          },
          /*
          * vue双向数据绑定特性，将页面的操作转变为对数据的操作
          * */
          addUser(user){
            let _this = this;
            // 若当前添加的用户已经在右侧列表 则不再添加
            let users = _this.roleUsers;
            for (let i = 0; i < users.length; i++){
              if (user === users[i]){
                return ;
              }
            }

            _this.roleUsers.push(user);

          },
          deleteUser(user){
            let _this = this;
            Tool.removeObj(_this.roleUsers,user);
          },
          /*保存用户角色信息*/
          saveRoleUsers(){
            let _this = this;
            let users = _this.roleUsers;
            Loading.show();

            let userIds = [];

            for (let i = 0; i < users.length; i++){
              userIds.push(users[i].id);
            }

            _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/role/saveroleusers',
        {
              roleId: _this.role.id,
              usersIds : userIds}).then((response) =>{
                Loading.hide();
                let resp = response.data;
                if (resp.success){
                  Toast.success("保存成功");
                }else {
                  Toast.error(resp.message);
                }
            })
          },
          listRoleUser: function () {
            let _this = this;
            let roleId = _this.role.id;

            _this.roleUsers = [];
            _this.$ajax.get(process.env.VUE_APP_SERVER + '/system/admin/role/listroleuser/'+ roleId)
                .then((response)=>{
                  Loading.hide();
                  // console.log("查询资源",response);
                  let resp = response.data;
                  let userIDs = resp.content;
                  console.log("加载角色关联记录的选中情况...")
                  console.log(userIDs);

                  for (let i = 0; i < userIDs.length; i++){
                    for (let j = 0; j < _this.users.length; j++){
                      if (userIDs[i] === _this.users[j].id){
                        _this.roleUsers.push(_this.users[j]);
                      }
                    }
                  }

                })
          }
        }

    }
</script>
