import Vue from "vue"
import Router from "vue-router"
import Index from "./views/index.vue"
import List from "./views/list.vue"
import Details from "./views/details";

Vue.use(Router)

// 子路由不添加斜杠信息
export default new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes:[{
        //访问任何页面都会定位到父路由index
        path: "*",
        redirect: "/index",
    }, {
        //访问index页面定位到Index组件
        path: "/index",
        component:Index
    },{
        path: "/list",
        component: List
    },{
        path: "/details",
        component: Details
    }
    ]
})