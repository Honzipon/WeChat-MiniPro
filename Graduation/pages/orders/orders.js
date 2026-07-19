const request = require('../../utils/request.js');

Page({
  data: {
    orders: [],
    loading: false,
    page: 1,
    hasMore: true,
    status: '' // 全部
  },
  onLoad() {
    this.loadOrders(true);
  },
  loadOrders(refresh = false) {
    if (this.data.loading || (!refresh && !this.data.hasMore)) return;
    this.setData({ loading: true });
    request.get('/order/list', {
      status: this.data.status,
      page: this.data.page,
      size: 10
    }, true).then(res => {
      const list = res.list || [];
      this.setData({
        orders: refresh ? list : this.data.orders.concat(list),
        hasMore: list.length === 10,
        loading: false,
        page: this.data.page + 1
      });
    }).catch(() => {
      this.setData({ loading: false });
    });
  },
  onReachBottom() {
    this.loadOrders();
  },
  changeTab(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({ status, page: 1, hasMore: true, orders: [] });
    this.loadOrders(true);
  },
  goToDetail(e) {
    const orderNo = e.currentTarget.dataset.orderno;
    wx.navigateTo({ url: `/pages/order/order?orderNo=${orderNo}` });
  }
});