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

        <pagination ref="pagination" v-bind:list = "list" v-bind:item-count="5">
        </pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
                <th>id</th>
                <th>相对路径</th>
                <th>文件名</th>
                <th>后缀</th>
                <th>大小</th>
                <th>用途</th>
<!--                <th>操作</th>-->
            </tr>
            </thead>

            <tbody>
            <tr v-for="file in files">

                        <td>{{file.id}}</td>
                        <td>{{file.path}}</td>
                        <td>{{file.name}}</td>
                        <td>{{file.suffix}}</td>
                        <td>{{file.size | formatFileSize}}</td>
                        <td>{{FILE_USE_ARRAY | optionKV(file.use)}}</td>

<!--                <td>-->
<!--                    <div class="hidden-sm hidden-xs btn-group">-->
<!--                        <button v-on:click="edit(file)" class="btn btn-xs btn-info">-->
<!--                            <i class="ace-icon fa fa-pencil bigger-120"></i>-->
<!--                        </button>-->
<!--                        <button v-on:click="dele(file.id)" class="btn btn-xs btn-danger">-->
<!--                            <i class="ace-icon fa fa-trash-o bigger-120"></i>-->
<!--                        </button>-->
<!--                    </div>-->
<!--                </td>-->
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
                              <label class="col-sm-2 control-label">相对路径</label>
                              <div class="col-sm-10">
                                  <input v-model="file.path" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">文件名</label>
                              <div class="col-sm-10">
                                  <input v-model="file.name" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">后缀</label>
                              <div class="col-sm-10">
                                  <input v-model="file.suffix" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                              <label class="col-sm-2 control-label">大小</label>
                              <div class="col-sm-10">
                                  <input v-model="file.size" type="text" class="form-control" >
                              </div>
                          </div>
                          <div class="form-group">
                            <label class="col-sm-2 control-label">用途</label>
                            <div class="col-sm-10">
                              <select v-model="file.use" class="form-control">
                                <option v-for="o in FILE_USE_ARRAY" v-bind:value="o.key">
                                  {{o.value}}
                                </option>
                              </select>
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
        name: "file",
        components: {Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                files: [],
                // 绑定新增方法的数据
                file: {},
                FILE_USE_ARRAY: FILE_USE_ARRAY,
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
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/file/queryfileinfo',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询文件",response);
                        _this.files = response.data.content.list;
                         _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.file = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                        || Validator.require(_this.file.path,"相对路径")
                        || Validator.length(_this.file.path,"相对路径",1,100)
                        || Validator.length(_this.file.name,"文件名",1,100)
                        || Validator.length(_this.file.suffix,"后缀",1,10)
                )
                // 校验数据通过，发送给后端
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/file/savefileinfo',_this.file).then((response)=>{
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
            edit(file){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.file = $.extend({},file);
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除文件后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/file/admin/file/deletefileinfo/'+ id)
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

            }
        }

    }
</script>
