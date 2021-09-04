<template>
    <div>
        <p>
<!--            <button v-on:click="add()" class="btn btn-white btn-default btn-round">-->
<!--                <i class="ace-icon fa fa-edit"></i>-->
<!--                新增-->
<!--            </button>-->
            &nbsp;
            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                刷新
            </button>
        </p>

      <div class="col-md-6">
        <ul id="tree" class="ztree"></ul>
      </div>

<!--        <pagination ref="pagination" v-bind:list = "list" v-bind:item-count="5">-->
<!--        </pagination>-->

<!--        <table id="simple-table" class="table  table-bordered table-hover">-->
<!--            <thead>-->
<!--            <tr>-->
<!--                <th>id</th>-->
<!--                <th>名称</th>-->
<!--                <th>页面</th>-->
<!--                <th>请求</th>-->
<!--                <th>父id</th>-->
<!--                <th>操作</th>-->
<!--            </tr>-->
<!--            </thead>-->

<!--            <tbody>-->
<!--            <tr v-for="resources in resourcess">-->

<!--                        <td>{{resources.id}}</td>-->
<!--                        <td>{{resources.name}}</td>-->
<!--                        <td>{{resources.page}}</td>-->
<!--                        <td>{{resources.request}}</td>-->
<!--                        <td>{{resources.parent}}</td>-->

<!--                <td>-->
<!--                    <div class="hidden-sm hidden-xs btn-group">-->
<!--&lt;!&ndash;                        <button v-on:click="edit(resources)" class="btn btn-xs btn-info">&ndash;&gt;-->
<!--&lt;!&ndash;                            <i class="ace-icon fa fa-pencil bigger-120"></i>&ndash;&gt;-->
<!--&lt;!&ndash;                        </button>&ndash;&gt;-->
<!--                        <button v-on:click="dele(resources.id)" class="btn btn-xs btn-danger">-->
<!--                            <i class="ace-icon fa fa-trash-o bigger-120"></i>-->
<!--                        </button>-->
<!--                    </div>-->
<!--                </td>-->
<!--            </tr>-->
<!--            </tbody>-->
<!--        </table>-->

<!--        <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">-->
<!--            <div class="modal-dialog" role="document">-->
<!--                <div class="modal-content">-->
<!--                    <div class="modal-header">-->
<!--                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
<!--                        <h4 class="modal-title">表单</h4>-->
<!--                    </div>-->
<!--                    <div class="modal-body">-->
<!--                        <form class="form-horizontal">-->

<!--                                        <div class="form-group">-->
<!--                                            <label class="col-sm-2 control-label">id</label>-->
<!--                                            <div class="col-sm-10">-->
<!--                                                <input v-model="resources.id" type="text" class="form-control" >-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="form-group">-->
<!--                                            <label class="col-sm-2 control-label">名称</label>-->
<!--                                            <div class="col-sm-10">-->
<!--                                                <input v-model="resources.name" type="text" class="form-control" >-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="form-group">-->
<!--                                            <label class="col-sm-2 control-label">页面</label>-->
<!--                                            <div class="col-sm-10">-->
<!--                                                <input v-model="resources.page" type="text" class="form-control" >-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="form-group">-->
<!--                                            <label class="col-sm-2 control-label">请求</label>-->
<!--                                            <div class="col-sm-10">-->
<!--                                                <input v-model="resources.request" type="text" class="form-control" >-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="form-group">-->
<!--                                            <label class="col-sm-2 control-label">父id</label>-->
<!--                                            <div class="col-sm-10">-->
<!--                                                <input v-model="resources.parent" type="text" class="form-control" >-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                        </form>-->
<!--                    </div>-->
<!--                    <div class="modal-footer">-->
<!--                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>-->
<!--                        <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>-->
<!--                    </div>-->
<!--                </div>&lt;!&ndash; /#form-modal-content &ndash;&gt;-->
<!--            </div>&lt;!&ndash; /#form-modal-dialog &ndash;&gt;-->
<!--        </div>&lt;!&ndash; /#form-modal &ndash;&gt;-->

    </div>
</template>

<script>
    // 导入pagination主键
    import Pagination from "../../components/pagination";
    export default {
        name: "resources",
        components: {Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                resourcess: [],
                // 绑定新增方法的数据
                resources: {},
                tre:{},
            }
        },
        // 页面初始化
        mounted: function (){
            let _this = this;
            // _this.$refs.pagination.size = 5;
            _this.list();
        },
        // 控件绑定方法
        methods : {
            // 代表查询第几页
            list() {
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
                    })
            },
            // add(){
            //     let _this = this;
            //     _this.resources = {};
            //     $("#form-modal").modal("show");
            // },
            // save(page) {
            //     let _this = this;
            //     // 添加校验
            //     if (1 != 1
            //             || Validator.require(_this.resources.id,"id")
            //             || Validator.require(_this.resources.name,"名称")
            //             || Validator.length(_this.resources.name,"名称",1,100)
            //             || Validator.length(_this.resources.page,"页面",1,50)
            //             || Validator.length(_this.resources.request,"请求",1,400)
            //     )
            //     // 校验数据通过，发送给后端
            //     Loading.show();
            //     _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/resources/saveresourcesinfo',_this.resources).then((response)=>{
            //             Loading.hide();
            //             console.log("保存数据",response);
            //             if (response.data.success){
            //                 $("#form-modal").modal("hide");
            //                 Toast.success("插入数据成功");
            //                 _this.list(1);
            //             }else {
            //                 $("#form-modal").modal("hide");
            //                 Toast.error("插入数据失败");
            //                 _this.list(1);
            //             }
            //         })
            // },
            // edit(resources){
            //     let _this = this;
            //     // 绑定数据字段 点击哪一行 获取哪一行的数据
            //     // 复制对象
            //     _this.resources = $.extend({},resources);
            //     // 展示编辑框
            //     $("#form-modal").modal("show");
            // },
            // dele(id){
            //     let _this = this;
            //     // 增加弹出框
            //     Confirm.show("删除资源后不可恢复确认删除!",function () {
            //         _this.$ajax
            //             .delete(process.env.VUE_APP_SERVER +  '/system/admin/resources/deleteresourcesinfo/'+ id)
            //             .then((response) => {
            //                 Loading.show();
            //                 if (response.data.success) {
            //                     Toast.error("删除数据成功");
            //                     _this.list(1);
            //                 } else {
            //                     _this.list(1);
            //                     Toast.error("删除数据失败");
            //                 }
            //                 Loading.hide();
            //             });
            //     })
            //
            // },
          initTree(){
            let _this = this;
            let setting = {
              data:{
                simpleData: {
                  idKey: "id",
                  pIdKey: "parent",
                  rootPId: "",
                  // enable: true
                }
              }
            };
            let zNodes = _this.resources;
            _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
            // 展开节点
            _this.tree.expandAll(true);

          },
        }

    }
</script>
