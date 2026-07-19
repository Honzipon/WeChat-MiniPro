const request = require('../../utils/request.js');

Page({
  data: {
    userInfo: null,
    stats: {
      orderCount: 0,
      favoriteCount: 0,
      couponCount: 0
    }
  },
  onShow() {
    this.loadUserInfo();
    this.loadStats();
  },
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({ userInfo });
  },
  loadStats() {
    request.get('/user/stats', {}, true).then(stats => {
      this.setData({ stats });
    });
  },
  goToOrders() {
    wx.switchTab({ url: '/pages/orders/orders' });
  },
  goToFavorites() {
    wx.navigateTo({ url: '/pages/favorite/favorite' });
  },
  goToCoupons() {
    wx.navigateTo({ url: '/pages/coupon/coupon' });
  },
  goToSettings() {
    wx.navigateTo({ url: '/pages/settings/settings' });
  },
  goToAbout() {
    wx.navigateTo({ url: '/pages/about/about' });
  },
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.reLaunch({ url: '/pages/login/login' });
        }
      }
    });
  }
});