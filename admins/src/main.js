import Vue from 'vue'
import App from './app.vue'
import router from './router'
import axios from "axios";
import filter from "./filter/filter";


Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

// 解决前后端分离项目，ajax请求sessionId不一致问题
axios.defaults.withCredentials = true

/***
 * axios拦截器
 */
axios.interceptors.request.use(function (config){
  console.log("请求:",config);
  // 为每个请求的header追加token信息
  let token = Tool.getLoginUser().token;
  if (Tool.isNotEmpty(token)){
    config.headers.token = token;
    console.log("请求headers追加token信息 ==>",token)
  }
  return config;
},error => {});

axios.interceptors.response.use(function (response){
  console.log("返回结果:",response);
  return response;
},error => {});


//全局过滤器
Object.keys(filter).forEach(key =>{
  Vue.filter(key,filter[key])
});

// 路由登陆拦截
// 跳转函数
router.beforeEach((to,from,next) =>{
//  要不要对meta.loginRequire属性做监控拦截 与router.js配置拦截内容一样
  if (to.matched.some(function (item){
    return item.meta.loginRequire;
  })){
    let loginUser = Tool.getLoginUser();
    if (Tool.isEmpty(loginUser)){
      next('/login');
    }else {
      next();
    }
  }else {
    next();
  }
});







new Vue({
  router,
  render: h => h(App),
}).$mount('#app');

console.log("环境: " + process.env.NODE_ENV)