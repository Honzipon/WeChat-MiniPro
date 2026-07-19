<template>
  <el-container style="height: 100vh;">
    <el-aside width="200px" style="background-color: #304156;">
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#fff" active-text-color="#ffd04b">
        <el-menu-item index="/venues">场馆管理</el-menu-item>
        <el-menu-item index="/matches">赛事管理</el-menu-item>
        <el-menu-item index="/orders">订单管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #ddd;">
        <span style="float: right; cursor: pointer;" @click="logout">退出登录</span>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('adminToken')
      this.$router.push('/login')
    }
  }
}
</script>