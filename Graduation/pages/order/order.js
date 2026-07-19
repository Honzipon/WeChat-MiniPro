const request = require('../../utils/request.js');

Page({
  data: {
    // 通用字段
    orderNo: null,           // 订单号（详情模式）
    order: null,             // 订单详情对象
    // 新建订单专用字段
    matchId: '',
    seatInfos: [],
    match: null,
    totalAmount: 0,
    contactName: '',
    contactPhone: '',
    note: '',
    submitting: false
  },

  onLoad(options) {
    const { orderNo, seats, matchId } = options;

    // 详情模式：有 orderNo
    if (orderNo) {
      this.setData({ orderNo });
      this.loadOrderDetail();
      return;
    }

    // 新建订单模式：有 seats 和 matchId
    if (matchId && seats) {
      try {
        const seatInfos = JSON.parse(seats);
        this.setData({ matchId, seatInfos });
        const total = seatInfos.reduce((sum, s) => sum + s.price, 0);
        this.setData({ totalAmount: total });
        this.loadMatchInfo();
      } catch (e) {
        console.error('解析座位参数失败', e);
        wx.showToast({ title: '数据解析失败', icon: 'none' });
      }
      return;
    }

    // 参数错误
    wx.showToast({ title: '参数错误', icon: 'none' });
  },

  // 加载赛事信息（新建订单模式）
  async loadMatchInfo() {
    try {
      const match = await request.get(`/match/${this.data.matchId}`, {}, true);
      this.setData({ match });
    } catch (err) {
      console.error('加载赛事信息失败', err);
      wx.showToast({ title: '加载赛事信息失败', icon: 'none' });
    }
  },

  // 加载订单详情（详情模式）
  async loadOrderDetail() {
    try {
      const order = await request.get(`/order/detail/${this.data.orderNo}`, {}, true);
      this.setData({ order });
      // 可选：解析座位信息用于展示
      if (order.seatIds) {
        const seatIds = order.seatIds.split(',').map(id => parseInt(id));
        // 可进一步请求座位详情，或直接显示座位ID（简化）
        this.setData({ seatInfos: seatIds.map(id => ({ id, seatNum: id })) });
      }
    } catch (err) {
      console.error('加载订单详情失败', err);
      wx.showToast({ title: '加载订单详情失败', icon: 'none' });
    }
  },

  // 联系人输入
  onNameInput(e) {
    this.setData({ contactName: e.detail.value });
  },
  onPhoneInput(e) {
    this.setData({ contactPhone: e.detail.value });
  },
  onNoteInput(e) {
    this.setData({ note: e.detail.value });
  },

  // 提交订单（新建订单模式）
  async submitOrder() {
    const { matchId, seatInfos, contactName, contactPhone, note } = this.data;
    if (!contactName || !contactPhone) {
      wx.showToast({ title: '请填写联系人信息', icon: 'none' });
      return;
    }
    if (this.data.submitting) return;
    this.setData({ submitting: true });

    try {
      const seatIds = seatInfos.map(s => s.id);
      const res = await request.post('/order/submit', {
        matchId,
        seatIds,
        contactName,
        contactPhone,
        note
      }, true);

      console.log('后端返回完整数据:', res);
      let orderNo = null;
      // 根据后端返回结构提取订单号（兼容多种格式）
      if (res && res.data && res.data.orderNo) {
        orderNo = res.data.orderNo;
      } else if (res && res.orderNo) {
        orderNo = res.orderNo;
      } else if (typeof res === 'string') {
        orderNo = res;
      }

      if (!orderNo) {
        wx.showToast({ title: '获取订单号失败', icon: 'none' });
        return;
      }

      // 跳转到订单详情页（使用 redirectTo 避免返回表单页）
      wx.redirectTo({
        url: `/pages/order/order?orderNo=${orderNo}`
      });
    } catch (err) {
      console.error('提交订单失败', err);
      wx.showToast({ title: err.message || '提交失败', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  },

  // 模拟支付（详情模式，可选）
  async payOrder() {
    const { orderNo } = this.data;
    if (!orderNo) return;
    try {
      const res = await request.post('/order/pay', { orderNo }, true);
      // 调起微信支付（此处简化，实际需要 res 中的支付参数）
      wx.requestPayment({
        timeStamp: res.timeStamp,
        nonceStr: res.nonceStr,
        package: res.package,
        signType: res.signType,
        paySign: res.paySign,
        success: () => {
          wx.showToast({ title: '支付成功' });
          this.loadOrderDetail(); // 刷新订单状态
        },
        fail: () => {
          wx.showToast({ title: '支付失败', icon: 'none' });
        }
      });
    } catch (err) {
      wx.showToast({ title: '支付失败', icon: 'none' });
    }
  }
});