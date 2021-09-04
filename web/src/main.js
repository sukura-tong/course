import Vue from 'vue'
import App from './app.vue'
import router from "@/router";
import axios from "axios";
import filter from "@/filter/filter";

Vue.config.productionTip = false
Vue.prototype.$ajax = axios;


// 解决前后端分离项目，ajax请求sessionId不一致问题
axios.defaults.withCredentials = true

/***
 * axios拦截器
 */
axios.interceptors.request.use(function (config){
  console.log("请求:",config);
  // // 为每个请求的header追加token信息
  // let token = Tool.getLoginUser().token;
  // if (Tool.isNotEmpty(token)){
  //   config.headers.token = token;
  //   console.log("请求headers追加token信息 ==>",token)
  // }
  return config;
},error => {});

axios.interceptors.response.use(function (response){
  console.log("返回结果:",response);
  return response;
},error => {});


// 引入全局过滤器 filter.js
Object.keys(filter).forEach(key =>{
  Vue.filter(key,filter[key])
});


new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
