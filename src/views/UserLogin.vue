<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>管理员登录</h2>
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="form.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名' }],
        password: [{ required: true, message: '请输入密码' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return;
        this.loading = true;
        try {
          const res = await this.$http.post('/admin/login', this.form);
          if (res.code === 0) {
            localStorage.setItem('adminToken', res.data.token);
            this.$message.success('登录成功');
            this.$router.push('/');
          } else {
            this.$message.error(res.message);
          }
        } catch (err) {
          this.$message.error('登录失败');
        } finally {
          this.loading = false;
        }
      });
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
</style>