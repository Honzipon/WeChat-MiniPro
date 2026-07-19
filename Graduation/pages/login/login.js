const auth = require('../../utils/auth.js');

Page({
  data: {},
  onLoad() {
    // 自动登录
    this.handleLogin();
  },
  handleLogin() {
    wx.showLoading({ title: '登录中' });
    wx.login({
      success: (res) => {
        if (res.code) {
          auth.login(res.code).then(() => {
            wx.hideLoading();
            wx.switchTab({ url: '/pages/index/index' });
          }).catch(() => {
            wx.hideLoading();
            wx.showToast({ title: '登录失败', icon: 'none' });
          });
        } else {
          wx.hideLoading();
          wx.showToast({ title: '登录失败', icon: 'none' });
        }
      }
    });
  }
});