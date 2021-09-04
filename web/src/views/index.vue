<template>
  <main role="main">

    <section class="jumbotron text-center">
      <div class="container">
        <h1>小鹿在线视频课程</h1>
        <p class="lead text-muted">
          知识付费时代已经到来了，这个行业具备崭新的发展空间，整个课程以实战为基础，手把手教会如何掌握一门生存技术。
        </p>
        <p>
          <router-link to="/list" class="btn btn-primary my-2 p-3">所有课程</router-link>
        </p>
      </div>
    </section>

    <div class="album py-5 bg-light">
      <div class="container">

          <div class="title1">最新上线</div>
          <div class="row">
            <div v-for="o in news" class="col-md-4">
              <the-course-card v-bind:course="o"></the-course-card>
            </div>
          </div>

          <hr>
          <div class="title1">好课推荐</div>
          <div class="row">
            <div v-for="o in news" class="col-md-4">
              <the-course-card v-bind:course="o"></the-course-card>
            </div>
          </div>

        </div>
      </div>
  </main>
</template>

<script>
import TheCourseCard from "../components/the-course-card";
export default {
  name: "index",
  components: {TheCourseCard},
  data:function (){
    return{
      news:[],
    }
  },
  mounted() {
    let _this = this;
    _this.listAllCourse();
  },

  methods:{
    listAllCourse(){
      let _this = this;
      // Loading.show();
      _this.$ajax
          .get(process.env.VUE_APP_SERVER + '/business/web/course/listnewcourse')
          .then((response => {
            // Loading.hide();
            let resp = response.data;
            console.log("课程查询结果:",resp)
            if (resp.success){
              _this.news = resp.content;
            }
          }))


    }

  },
}
</script>

<style>
.title1{
  margin-bottom: 2rem;
  color: #fafafa;
  letter-spacing: 0;
  text-shadow: 0px 1px 0px #999, 0px 2px 0px #888, 0px 3px 0px #777, 0px 4px 0px #666, 0px 5px 0px #555, 0px 6px 0px #444, 0px 7px 0px #333, 0px 8px 7px #001135;
  font-size: 2rem;
}
.title2{
  margin-bottom: 2rem;
  color: transparent;
  -webkit-text-stroke: 1px black;
  letter-spacing: 0.04em;
  font-size: 2rem;
}
</style>
