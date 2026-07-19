// app.js
const auth = require('./utils/auth.js');

App({
  onLaunch() {
    // 检查是否已登录
    const token = wx.getStorageSync('token');
    if (!token) {
      auth.login().then(() => {
        console.log('登录成功');
      }).catch(err => {
        console.error('登录失败', err);
      });
    }
  },
  globalData: {
    userInfo: null
  }
});