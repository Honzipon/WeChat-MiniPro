const request = require('../../utils/request.js');

Page({
  data: {
    dateList: [],
    selectedDate: '',
    matches: [],
    loading: false,
    page: 1,
    hasMore: true
  },
  onLoad() {
    this.generateDateList();
    this.loadMatches(true);
  },
  generateDateList() {
    const dates = [];
    const today = new Date();
    for (let i = 0; i < 7; i++) {
      const date = new Date(today);
      date.setDate(today.getDate() + i);
      const dateStr = date.toISOString().split('T')[0];
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      dates.push({
        date: dateStr,
        weekday: weekdays[date.getDay()],
        day: date.getDate()
      });
    }
    this.setData({ dateList: dates, selectedDate: dates[0].date });
  },
  selectDate(e) {
    const date = e.currentTarget.dataset.date;
    this.setData({ selectedDate: date, page: 1, hasMore: true, matches: [] });
    this.loadMatches(true);
  },
  loadMatches(refresh = false) {
    if (this.data.loading || (!refresh && !this.data.hasMore)) return;
    this.setData({ loading: true });
    request.get('/match/list', {
      date: this.data.selectedDate,
      page: this.data.page,
      size: 10
    }).then(res => {
      const list = res.list || [];
      this.setData({
        matches: refresh ? list : this.data.matches.concat(list),
        hasMore: list.length === 10,
        loading: false,
        page: this.data.page + 1
      });
    }).catch(() => {
      this.setData({ loading: false });
    });
  },
  onReachBottom() {
    this.loadMatches();
  },
  goToMatchDetail(e) {
    const { id, venueId } = e.currentTarget.dataset;
    if (!venueId) {
      wx.showToast({ title: '赛事数据缺少场馆ID', icon: 'none' });
      console.error('venueId is missing', e.currentTarget.dataset);
      return;
    }
    wx.navigateTo({
      url: `/pages/venue/venue?matchId=${id}&venueId=${venueId}`
    });
  }
});