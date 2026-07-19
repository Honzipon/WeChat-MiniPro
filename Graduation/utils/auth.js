const request = require('./request.js');

const login = () => {
  return new Promise((resolve, reject) => {
    wx.login({
      success: (res) => {
        request.post('/user/login', { code: res.code }, false).then(data => {
          wx.setStorageSync('token', data.token);
          wx.setStorageSync('userInfo', data.userInfo);
          resolve(data);
        }).catch(reject);
      },
      fail: reject
    });
  });
};

module.exports = { login };