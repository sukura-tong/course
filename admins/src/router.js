import Vue from "vue"
import Router from "vue-router"
import Login from "./views/login.vue"
import Admin from "./views/admin.vue"
import Welcome from "./views/admin/welcome.vue"
import Chapter from "./views/admin/chapter.vue"
import Section from "./views/admin/section.vue"
import Course from "./views/admin/course.vue"
import Category from "./views/admin/category.vue"
import Teacher from "./views/admin/teacher.vue"
import User from "./views/admin/user.vue"
import Content from "./views/admin/content.vue"
import Resources from "./views/admin/resources.vue"
import File from "./views/admin/file.vue"
import Role from "./views/admin/role.vue"

Vue.use(Router)

// 子路由不添加斜杠信息
export default new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes:[{
        path: "*",
        redirect: "/login",
    },{
        path: "",
        redirect: "/login",
    }, {
        path: "/login",
        component: Login,
    },{
        path: "/",
        name: "admin",
        component: Admin,
        meta:{
         //   对父路由Admin做登陆拦截，子路由自动获取拦截内容
         loginRequire: true
        },
        children:[{
            path: "welcome",
            name: "welcome",
            component: Welcome,
        },{
            path: "business/chapter",
            name: "business/chapter",
            component: Chapter,
        },{
            path: "business/section",
            name: "business/section",
            component: Section,
        },{
            path: "business/course",
            name: "business/course",
            component: Course,
        },{
            path: "business/category",
            name: "business/category",
            component: Category,
        },{
            path: "business/teacher",
            name: "business/teacher",
            component: Teacher,
        },{
            path: "business/content",
            name: "business/content",
            component: Content,
        },{
            path: "system/user",
            name: "system/user",
            component: User,
        },{
            path: "system/resources",
            name: "system/resources",
            component: Resources,
        },{
            path: "file/file",
            name: "file/file",
            component: File,
        },{
            path: "system/role",
            name: "system/role",
            component: Role,
        },
        ]
    }

    ]
})