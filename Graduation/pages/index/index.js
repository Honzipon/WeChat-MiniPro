const request = require('../../utils/request.js');

Page({
  data: {
    banners: [],
    upcomingMatches: [],
    hotMatches: []
  },
  onLoad() {
    this.loadBanners();
    this.loadMatches();
  },
  loadBanners() {
    request.get('/banner/list').then(banners => {
      this.setData({ banners });
    });
  },
  loadMatches() {
    request.get('/match/upcoming', { limit: 3 }).then(matches => {
      this.setData({ upcomingMatches: matches });
    });
    request.get('/match/hot', { limit: 4 }).then(matches => {
      this.setData({ hotMatches: matches });
    });
  },
  goToMatchDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/match/match?id=${id}` });
  },
  goToVenue(e) {
    const { id, venueId } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/venue/venue?matchId=${id}&venueId=${venueId}`
    });
  }
});