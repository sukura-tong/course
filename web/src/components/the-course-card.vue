<template>
  <div class="card mb-4 shadow-sm course">
    <img v-bind:src="course.image" class="img-fluid">

    <div class="card-body">
      <h4 class="">{{course.name}}</h4>
      <p class="card-text">{{course.summary}}</p>

      <div class="d-flex justify-content-between align-items-center">
        <div class="btn-group">
          <router-link v-bind:to="'/details?id='+ course.id" class="btn btn-outline-secondary">
            课程详情
          </router-link>
        </div>

        <div class="text-muted">
                    <span class="badge badge-info">
                      <i class="fa fa-yen"></i>
                        &nbsp;{{course.price}}
                    </span>
          &nbsp;&nbsp;
          <span class="badge badge-info">
                      <i class="fa fa-user"></i>
                      {{course.enroll}}
                    </span>
          &nbsp;
          <span class="badge badge-info">
                     {{COURSE_LEVEL | optionKV(course.level)}}
                    </span>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "theCourseCard",
  props:{
    course:{}
  },
  data:function (){
    return{
      COURSE_LEVEL:COURSE_LEVEL,
    }
  },
}
</script>

<style scoped>
  .course h4{
    font-size: 1.25rem;
    margin-top: 15px;
  }
  .course .text-muted .badge{
    font-size: 1rem;
  }
</style>