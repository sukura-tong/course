<template>
  <main role="main">
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row course-head">
          <div class="col-sm-6" id="cover-video-div">
            <img v-bind:src="course.image" class="img-fluid">
          </div>
          <div class="col-sm-6">
            <h1>{{course.name}}</h1>
            <p class="course-head-item">
              <span><i class="fa fa-clock-o">{{course.name}}</i></span>
              <span>{{COURSE_LEVEL | optionKV(course.level)}}</span>
              <span><i class="fa fa-user">{{course.enroll}}</i></span>
            </p>
            <p class="course-head-desc">{{course.summary}}</p>
            <p class="course-head-price">
              <span class="price-now text-danger">
                <i class="fa fa-yen">{{course.price}}</i>
              </span>
            </p>
            <p class="course-head-buttons-links">
              <a class="btn btn-lg badge-primary" href="javascript:;">立即报名</a>
            </p>
          </div>
        </div>

       <div class="row">
         <div class="col-sm-9">
           <!-- nac tab-->
           <ul class="nav nav-tabs">
             <li class="nav-item">
               <a class="nav-link active" href="#info" data-toggle="tab">课程介绍</a>
             </li>
             <li class="nav-item">
               <a class="nav-link" href="#chapter" data-toggle="tab">章节目录</a>
             </li>
           </ul>

           <br>
           <!--nav tab panes-->
           <div class="tab-content">
             <div class="tab-pane active" id="info" v-html="course.content">
             </div>
             <div class="tab-pane" id="chapter">
               <div v-for="(chapter,i) in chapters" class="chapter">
                 <div v-on:click="doFolded(chapter,i)" class="chapter-chapter">
                   <span class="folded-button">{{chapter.name}}</span>
                   <span class="pull-right">
                      <i v-show="chapter.folded" class="fa fa-plus-square" aria-hidden="true"></i>
                      <i v-show="!chapter.folded" class="fa fa-minus-square" aria-hidden="true"></i>
                    </span>
                 </div>
                 <div v-show="!chapter.folded">
                   <table class="table table-striped">
                     <tr v-for="(s, j) in chapter.sections" class="chapter-section-tr">
                       <td class="col-sm-8 col-xs-12">
                         <div v-on:click="play(s)" class="section-title">
                           <i class="fa fa-video-camera d-none d-sm-inline"></i>&nbsp;&nbsp;
                           <span class="d-none d-sm-inline">第{{j+1}}节&nbsp;&nbsp;</span>
                           {{s.title}}
                           <span v-show="s.charge !== SECTION_CHARGE.CHARGE.key"
                                 class="badge badge-primary hidden-xs">免费</span>
                         </div>
                       </td>
                       <td class="col-sm-1 text-right">
                         <span class="badge badge-primary">{{s.time | formatSecond}}</span>
                       </td>
                     </tr>
                   </table>
                 </div>
               </div>
             </div>
           </div>

         </div>

         <div class="col-sm-3">
           <div class="card" style="width: 18rem;">
             <img v-bind:src="teacher.image" class="card-img-top">
             <div class="card-body">
               <h5 class="card-title">{{teacher.name}}</h5>
               <p class="card-text">{{teacher.motto}}</p>
               <p class="card-text">{{teacher.intro}}</p>
             </div>
           </div>
         </div>
       </div>

      </div>
    </div>
    <modal-player ref="modalPlayer"></modal-player>

  </main>
</template>

<script>
import ModalPlayer from "../components/modal-player";
export default {
  name: "details",
  components: {ModalPlayer},
  data:function (){
    return{
      course:[],
      teacher:{},
      chapters:[],
      sections:[],
      COURSE_LEVEL:COURSE_LEVEL,
      SECTION_CHARGE:SECTION_CHARGE,
    }
  },
  mounted() {
    let _this = this;
    // 从地址栏获取上一界面跳转来的课程ID号
    // let courseId = 'W99G8iaB';
    let courseId = _this.$route.query.id;
    _this.listAllCourseByCorseId(courseId)
  },
  methods:{
    listAllCourseByCorseId(courseId){
      let _this = this;
      _this.$ajax
          .get(process.env.VUE_APP_SERVER + '/business/web/course/findcoursebyid/'+ courseId)
          .then((response)=>{
            let res = response.data;
            console.log("课程详情内容:");
            console.log(res.content);
            _this.course = res.content;
            let teacher = res.content.teacher;
            if (Tool.isEmpty(teacher)){
              _this.teacher = {};
            }else {
              _this.teacher = teacher;
            }
            console.log("teacher:",teacher);

            _this.chapters = res.content.chapters;
            _this.sections = res.content.sections;

            console.log("chapters:",_this.chapters);
            console.log("sections:",_this.sections);

            // 将所有的小节存放到对应的大章内
            for (let  i = 0; i < _this.chapters.length; i++){
              let c = _this.chapters[i];
              c.sections = [];
              for (let j = 0; j < _this.sections.length; j++){
                let s = _this.sections[j];
                if (c.id === s.chapterId){
                  c.sections.push(s);
                }
              }

            }
            console.log("chapters:",_this.chapters);
            console.log("sections:",_this.sections);
          })
    },
    play(section){
      let _this = this;
      if (section.charge === _this.SECTION_CHARGE.CHARGE.key){
        Toast.warning("请登录后操作...")
      }else {
        _this.$refs.modalPlayer.playVod(section.id)
      }

    },
    doFolded(chapter,i){
      let _this = this;
      chapter.folded = !chapter.folded;
      //使用$set设置
      // 将chapter重新赋值给i位置的元素
      _this.$set(_this.chapters,i,chapter);
    }
  }
}
</script>

<style>
/* 课程信息 */
.course-head {
}
.course-head h1 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
}
.course-head-item span {
  margin-right: 1rem;
}
.course-head-desc {
  font-size: 1rem;
  color: #555
}
.course-head a {
}
.course-head-price {
  font-size: 2rem;
}
@media (max-width: 700px) {
  .course-head h1 {
    font-size: 1.5rem;
  }
}

/* 章节列表 */
.chapter {
  padding-bottom: 1.25rem;
}
.chapter-chapter {
  font-size: 1.25rem;
  padding: 1.25rem;
  background-color: #23527c;
  color: white;
  cursor: pointer;
}
.chapter-section-tr {
  font-size: 1rem;
}
.chapter-section-tr td{
  padding: 1rem 1.25rem;
  vertical-align: middle;
}
/*鼠标手势*/
.chapter-section-tr td .section-title{
  color: #555;
}
.chapter-section-tr td .section-title:hover{
  color: #23527c;
  font-weight: bolder;
  cursor: pointer;
}
/*行头小图标*/
.chapter-section-tr td .section-title i{
  color: #2a6496;
}
@media (max-width: 700px) {
  .chapter-chapter {
    font-size: 1.2rem;
  }
  .chapter-section-tr {
    font-size: 0.9rem;
  }
}
</style>
