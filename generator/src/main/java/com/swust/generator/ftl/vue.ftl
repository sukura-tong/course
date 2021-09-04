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
                <#list fieldList as field>
                <th>${field.nameCn}</th>
                </#list>
                <th>操作</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="${domain} in ${domain}s">

                <#list fieldList as field>
                    <#if field.nameHump != "createdAt" && field.nameHump != "updatedAt">
                        <td>{{${domain}.${field.nameHump}}}</td>
                    </#if>
                </#list>

                <td>
                    <div class="hidden-sm hidden-xs btn-group">
                        <button v-on:click="edit(${domain})" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-pencil bigger-120"></i>
                        </button>
                        <button v-on:click="dele(${domain}.id)" class="btn btn-xs btn-danger">
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

                            <#list fieldList as field>
                                    <#if field.nameHump != "createdAt" && field.nameHump != "updatedAt">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">${field.nameCn}</label>
                                            <div class="col-sm-10">
                                                <input v-model="${domain}.${field.nameHump}" type="text" class="form-control" >
                                            </div>
                                        </div>
                                    </#if>
                            </#list>
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
        name: "${domain}",
        components: {Pagination},
        // 定义方法的数据绑定
        data: function (){
            return{
                // 绑定查询方法的返回数据
                ${domain}s: [],
                // 绑定新增方法的数据
                ${domain}: {}
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
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/${module}/admin/${domain}/query${domain}info',{
                    page: page,
                    size: _this.$refs.pagination.size,
                }).then((response)=>{
                        Loading.hide();
                        // console.log("查询${tableNameCn}",response);
                        _this.${domain}s = response.data.content.list;
                         _this.$refs.pagination.render(page,response.data.content.total);
                    })
            },
            add(){
                let _this = this;
                _this.${domain} = {};
                $("#form-modal").modal("show");
            },
            save(page) {
                let _this = this;
                // 添加校验
                if (1 != 1
                <#list fieldList as field>
                    <#if !field.nullAble>
                        || Validator.require(_this.${domain}.${field.nameHump},"${field.nameCn}")
                    </#if>
                    <#if (field.length > 0)>
                        || Validator.length(_this.${domain}.${field.nameHump},"${field.nameCn}",1,${field.length?c})
                    </#if>
                </#list>
                )
                // 校验数据通过，发送给后端
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/${module}/admin/${domain}/save${domain}info',_this.${domain}).then((response)=>{
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
            edit(${domain}){
                let _this = this;
                // 绑定数据字段 点击哪一行 获取哪一行的数据
                // 复制对象
                _this.${domain} = $.extend({},${domain});
                // 展示编辑框
                $("#form-modal").modal("show");
            },
            dele(id){
                let _this = this;
                // 增加弹出框
                Confirm.show("删除${tableNameCn}后不可恢复确认删除!",function () {
                    _this.$ajax
                        .delete(process.env.VUE_APP_SERVER +  '/${module}/admin/${domain}/delete${domain}info/'+ id)
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
