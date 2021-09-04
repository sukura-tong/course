<template>
  <header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <router-link to="/index" class="navbar-brand" >
          <i class="ace-icon fa fa-video-camera"></i>&nbsp;小鹿在线课程
        </router-link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <router-link to="/index" class="nav-link" >主页 <span class="sr-only">(current)</span></router-link>
            </li>
            <li class="nav-item">
              <router-link to="/list" class="nav-link">全部课程</router-link>
            </li>
            <li class="nav-item active">
              <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                更多
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="#">关于我们</a>
                <a class="dropdown-item" href="#">合作渠道</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#">更多信息</a>
              </div>
            </li>
          </ul>
          <span class="text-white"></span>
          <button class="btn btn-outline-light my-2 my-sm-0" type="submit">登陆/注册</button>
        </div>
      </div>
    </nav>

  </header>
</template>

<script>
export default {
  name: "theHeader"
}
</script>

<style scoped>

</style>