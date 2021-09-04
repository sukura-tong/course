<template>
  <div class="main-container">
    <div class="main-content">
      <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
          <div class="login-container">
            <div class="center">
              <h1>
                <i class="ace-icon fa fa-leaf green"></i>
                <span class="red">控制管理</span>
                <span class="white" id="id-text2"></span>
              </h1>
            </div>

            <div class="space-6"></div>

            <div class="position-relative">
              <div id="login-box" class="login-box visible widget-box no-border">
                <div class="widget-body">
                  <div class="widget-main">
                    <h4 class="header blue lighter bigger">
                      <i class="ace-icon fa fa-coffee green"></i>
                      请输入您的登录信息
                    </h4>

                    <div class="space-6"></div>

                    <form>
                      <fieldset>
<!--                        使用v-module给控件绑定属性-->
                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input v-model = "user.loginName"
                                  type="text" class="form-control" placeholder="用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
                        </label>

                        <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input v-model = "user.password"
                                  type="password" class="form-control" placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
                        </label>

<!--                        yanzhengma-->
                        <label class="block clearfix">
                            <span class="block input-icon input-icon-right">
                                <div class="input-group">
                                  <input v-model="user.imageCode" type="text" class="form-control" placeholder="验证码">
                                  <span class="input-group-addon" id="basic-addon2">
                                    <img v-on:click="loadImageCode()" id="image-code" alt="验证码">
                                  </span>
                                </div>
                            </span>
                        </label>

                        <div class="space"></div>

                        <div class="clearfix">
                          <label class="inline">
<!--                            使用v-model绑定变量-->
                            <input v-model="remeber"
                                type="checkbox" class="ace" />
                            <span class="lbl"> 记住我 </span>
                          </label>

                          <button type="button"
                                  class="width-35 pull-right btn btn-sm btn-primary"
                            v-on:click="login()" >
                            <i class="ace-icon fa fa-key"></i>
                            <span class="bigger-110">登录</span>
                          </button>
                        </div>

                        <div class="space-4"></div>
                      </fieldset>
                    </form>

                  </div><!-- /.widget-main -->

                </div><!-- /.widget-body -->
              </div><!-- /.login-box -->

            </div><!-- /.position-relative -->

            <div class="navbar-fixed-top align-right">
              <br />
              &nbsp;
              <a id="btn-login-dark" href="#">Dark</a>
              &nbsp;
              <span class="blue">/</span>
              &nbsp;
              <a id="btn-login-blur" href="#">Blur</a>
              &nbsp;
              <span class="blue">/</span>
              &nbsp;
              <a id="btn-login-light" href="#">Light</a>
              &nbsp; &nbsp; &nbsp;
            </div>
          </div>
        </div><!-- /.col -->
      </div><!-- /.row -->
    </div><!-- /.main-content -->
  </div><!-- /.main-container -->
</template>

<script>
export default {
  name: "login",
  // 定义主键变量
  data:function (){
    return{
      user:{},
      remeber:{},
      imageCodeToken:'',
    }
  },
  mounted: function (){
    let _this = this;

    $('body').removeClass('no-skin');
    $('body').attr('class', 'login-layout light-login');
    //初始化页面 从缓存中获取登陆信息
    let localLoginInfo = LocalStorage.get(LOCAL_KEY_LOGIN_USER_REMEBER);
    if (!Tool.isEmpty(localLoginInfo)){
      _this.user = localLoginInfo;
    }
    _this.loadImageCode();

  },
  methods:{
    //与 v-on对应 给控件追加方法
    login(page) {
      let _this = this;
      // 添加校验
      // if (1 != 1
      //     // || Validator.require(_this.user.id,"id")
      //     || Validator.require(_this.user.loginName,"登录名")
      //     || Validator.length(_this.user.loginName,"登录名",1,50)
      //     || Validator.length(_this.user.name,"昵称",1,50)
      //     || Validator.require(_this.user.password,"密码")
      // )
          // 校验数据通过，发送给后端

      //使用md5加密密码
      // let showPassword = _this.user.password;

      let remberUser = LocalStorage.get(LOCAL_KEY_LOGIN_USER_REMEBER) || {};
      let md5 = hex_md5(_this.user.password);
      // 如果密码是从缓存中获取 就不需要二次加密
      if (md5 !== remberUser.md5){
        _this.user.password = hex_md5(_this.user.password + KEY);

      }
      // 添加验证码属性
      _this.user.imageCodeToken = _this.imageCodeToken;

      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/userlogin',_this.user).then((response)=>{
        Loading.hide();
        if (response.data.success){
          // 使用会话缓存进行登录保存
          let content = response.data.content;
          Tool.setLoginUser(content);
           // 如果勾选记住我 将登录信息添加至本地缓存
          if (_this.remeber){

            let remmd5 = hex_md5(_this.user.password);

            LocalStorage.set(LOCAL_KEY_LOGIN_USER_REMEBER,{
              loginName: content.loginName,
              password :  _this.user.password,
              md5:remmd5,
            })
          }else {
            // 如果不清空缓存 则本地缓存一直存在 一直可以获取到数据
            LocalStorage.set(LOCAL_KEY_LOGIN_USER_REMEBER,null);
          }
            _this.$router.push("/welcome");
        }else {
          Toast.warning(response.data.message);
          _this.user.password = "";
          _this.$router.push("/login");
        }
      })
    },
    loadImageCode(){
      let _this = this;
      let imageCodeToken = Tool.uuid(8);
      _this.imageCodeToken = imageCodeToken;
      $('#image-code').attr('src',process.env.VUE_APP_SERVER + '/system/admin/kaptcha/imagecode/' + imageCodeToken);
    },
  }
}
</script>

<style scoped>
  .input-group-addon{
    /*设置边界*/
    padding: 0;
  }
</style>
