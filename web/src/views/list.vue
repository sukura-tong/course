<template>
  <main role="main">

    <div class="header-nav">
      <div class="clearfix">
        <div class="container">
          <div class="row">
            <div class="col-12">
              <a v-on:click="onClickLevelOne('00000000')"
                 id="category-00000000"
                 href="javascript:;"
                 class="cur">全部</a>
              <a v-for="o in levelOne"
                 v-on:click="onClickLevelOne(o.id)"
                 v-bind:id="'category-' + o.id"
                 href="javascript:;">{{o.name}}</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="skill clearfix">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <a v-on:click="onClickLevelTwo('11111111')"
               id="category-11111111"
               href="javascript:;" class="on">不限</a>
            <a v-for="o in levelTwo"
               v-on:click="onClickLevelTwo(o.id)"
               v-bind:id="'category-' + o.id"
               href="javascript:;">{{o.name}}</a>
            <div style="clear: both"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="album py-5 bg-light">
      <div class="container">
        <!--分页-->
        <pagination ref="pagination" v-bind:list="listAllCourse"></pagination>
        <hr>
          <div class="row">
            <div v-for="o in courses" class="col-md-4">
              <the-course-card v-bind:course="o"></the-course-card>
            </div>
            <h3 v-show="courses.length === 0">课程还未上映</h3>
          </div>
      </div>
    </div>
  </main>
</template>

<script>
import TheCourseCard from "../components/the-course-card";
import Pagination from "../components/pagination";
export default {
  name: "index",
  components: {Pagination, TheCourseCard},
  data:function (){
    return{
      courses: [],
      levelOne: [],
      levelTwo: [],
      categorys: [],
      level1Id : "",
      level2Id:"",
    }
  },
  mounted() {
    let _this = this;
    _this.$refs.pagination.size = 1;
    _this.listAllCourse(1);
    _this.listAllCategory();
  },

  methods:{
    listAllCourse(page){
      let _this = this;
      // Loading.show();
      _this.$ajax
          .post(process.env.VUE_APP_SERVER + '/business/web/course/listallcourse',{
            status:'',
            page:page,
            size:_this.$refs.pagination.size,
            // 优先查询level2Id
            categoryId:_this.level2Id || _this.level1Id || "",
          })
          .then((response => {
            // Loading.hide();
            let resp = response.data;
            console.log("课程查询结果:",resp)
            if (resp.success){
              _this.courses = resp.content;
            }
          }))
    },

    listAllCategory() {
      let _this = this;
      // 展开提示框 防止用户恶意点击
      // Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/course/listallcategory')
          .then((response)=>{
            // Loading.hide();
            console.log("查询分类表",response.data);
            _this.categorys = response.data.content.list;
            // 将所有记录格式化为树形结构
            // 将数据清空
            _this.levelOne = [];
            _this.levelTwo = [];
            for (let i = 0; i <_this.categorys.length; i++){
              let c = _this.categorys[i];
              if (c.parent === '00000000'){
                _this.levelOne.push(c);
              }else {
                _this.levelTwo.push(c);
              }
            }
          })
    },

    onClickLevelOne(levelOneId){
      let _this = this;

      _this.level2Id = null;
      _this.level1Id = levelOneId;
      if (levelOneId === "00000000"){
       _this.level1Id = null;
      }
      // 点击一级分类显示激活状态
        // 去除兄弟节点激活样式
      $('#category-'+ levelOneId).siblings("a").removeClass("cur");
        // 自身增加激活样式
      $('#category-' + levelOneId).addClass("cur");
      // 点击一级分类时，二级分类无限按钮设置激活状态
      $('#category-11111111').siblings("a").removeClass("on");
      $('#category-11111111').addClass("on");

      // 将level2中的内容清空、在添加内容
      _this.levelTwo = [];
      let categorys = _this.categorys;

      // 点击全部选择所有二级分类
      if (levelOneId === "00000000"){
        for (let i = 0; i < categorys.length; i++){
            let c = categorys[i];
            if (c.parent != "00000000"){
              _this.levelTwo.push(c);
            }
        }
      }
      // 点击特定分类ID则筛选
      if (levelOneId !== "00000000"){
        for (let i = 0; i < categorys.length; i++){
          let c = categorys[i];
          if (c.parent === levelOneId){
            _this.levelTwo.push(c);
          }
        }
      }
      // 每次点击重新查询课程参数
      _this.listAllCourse(1);

    },

    onClickLevelTwo(levelTwoId){
      let _this = this;

      _this.level2Id = levelTwoId;
      if (levelTwoId === "11111111"){
        _this.level2Id = null;
      }

      // 点击二级分类显示激活状态
      $("#category-"+levelTwoId).siblings("a").removeClass("on");
      $("#category-"+levelTwoId).addClass("on");


      _this.listAllCourse(1);
    },


  },
}
</script>

<style>
 /* 头部 一级分类 */
 .header-nav {
   height: auto;
   background: #fff;
   box-shadow: 0 8px 16px 0 rgba(28,31,33,.1);
   padding: 16px 0;
   box-sizing: border-box;
   position: relative;
   z-index: 1;
   /*background-color: #d6e9c6;*/
 }
.header-nav>div {
  width: 100%;
  padding-left: 12px;
  box-sizing: border-box;
  margin-left: auto;
  margin-right: auto;
  /*background-color: #B4D5AC;*/
}
.header-nav a {
  float: left;
  font-size: 16px;
  color: #07111b;
  line-height: 50px;
  height: 45px;
  position: relative;
  margin-right: 46px;
  font-weight: 700;
}
.header-nav a:hover {
  color: #c80;
}
.header-nav a.cur {
  color: #c80;
}
.header-nav a.cur:before {
  display: block;
}
.header-nav a:before {
  display: none;
  content: ' ';
  position: absolute;
  bottom: 0;
  background: #c80;
  width: 16px;
  height: 3px;
  left: 50%;
  margin-left: -8px;
}
/* 二级分类 */
.skill {
  width: 100%;
  padding: 24px 0 0;
  position: relative;
  margin: 0 auto;
}
.skill a.on {
  color: #c80;
  background: rgba(204,136,0,.1);
}
.skill a {
  float: left;
  margin-right: 20px;
  padding: 0 12px;
  font-size: 14px;
  color: #4d555d;
  line-height: 32px;
  border-radius: 6px;
  margin-bottom: 12px;
}
.skill a:hover {
  background: #d9dde1;
}
</style>
