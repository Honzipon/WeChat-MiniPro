const request = require('../../utils/request.js');

Page({
  data: {
    matchId: null,
    venueId: null,
    areas: [],              // [{ name }]
    rows: [],               // 排号列表（数字）
    seatOptions: [],        // [{ seatNum, price, id, label }]
    selectedAreaName: '',
    selectedRow: null,
    selectedSeatLabel: '',
    selectedSeat: null      // 完整的座位对象
  },

  onLoad(options) {
    const { matchId, venueId } = options;
    if (!matchId || !venueId) {
      wx.showToast({ title: '参数错误', icon: 'none' });
      return;
    }
    this.setData({ matchId, venueId });
    this.loadSeats();
  },

  async loadSeats() {
    try {
      wx.showLoading({ title: '加载座位数据...' });
      const seats = await request.get(`/venue/${this.data.venueId}/seats`, { matchId: this.data.matchId }, true);
      if (!seats || seats.length === 0) {
        wx.hideLoading();
        wx.showToast({ title: '暂无座位数据', icon: 'none' });
        return;
      }
      // 缓存所有座位
      this.allSeats = seats;

      // 提取区域（去重，按出现顺序）
      const areaMap = new Map();
      seats.forEach(seat => {
        if (seat.area && !areaMap.has(seat.area)) {
          areaMap.set(seat.area, { name: seat.area });
        }
      });
      const areas = Array.from(areaMap.values());
      this.setData({ areas });

      wx.hideLoading();
    } catch (err) {
      console.error(err);
      wx.hideLoading();
      wx.showToast({ title: '加载失败', icon: 'none' });
    }
  },

  // 区域选择
  onAreaChange(e) {
    const index = e.detail.value;
    const area = this.data.areas[index];
    const areaName = area.name;
    // 筛选该区域下的所有座位
    const areaSeats = this.allSeats.filter(s => s.area === areaName);
    // 提取排号（去重，排序）
    const rows = [...new Set(areaSeats.map(s => s.rowNum))].sort((a, b) => a - b);
    this.setData({
      selectedAreaName: areaName,
      rows: rows,
      selectedRow: null,
      seatOptions: [],
      selectedSeatLabel: '',
      selectedSeat: null
    });
  },

  // 排号选择
  onRowChange(e) {
    const index = e.detail.value;
    const row = this.data.rows[index];
    // 获取该排下所有可售座位
    const rowSeats = this.allSeats.filter(s => s.area === this.data.selectedAreaName && s.rowNum === row && s.status === 0);
    const seatOptions = rowSeats.map(s => ({
      seatNum: s.seatNum,
      price: s.price,
      id: s.id,
      label: `${s.seatNum}号 (¥${s.price})`
    }));
    this.setData({
      selectedRow: row,
      seatOptions: seatOptions,
      selectedSeatLabel: '',
      selectedSeat: null
    });
  },

  // 座号选择
  onSeatChange(e) {
    const index = e.detail.value;
    const seatOption = this.data.seatOptions[index];
    // 找到完整的座位对象
    const fullSeat = this.allSeats.find(s => s.id === seatOption.id);
    this.setData({
      selectedSeatLabel: seatOption.label,
      selectedSeat: fullSeat
    });
  },

  // 添加到订单（立即预订）
  addToCart() {
    const seat = this.data.selectedSeat;
    if (!seat) return;
    // 只传递必要的字段，避免循环引用
    const seatInfo = {
      id: seat.id,
      area: seat.area,
      rowNum: seat.rowNum,
      seatNum: seat.seatNum,
      price: seat.price
    };
    wx.navigateTo({
      url: `/pages/order/order?matchId=${this.data.matchId}&seats=${JSON.stringify([seatInfo])}`
    });
  }
});