const BASE_URL = 'http://localhost:8080/api'; // 开发时用本地IP，上线改为域名

const request = (url, method, data, token = false) => {
  return new Promise((resolve, reject) => {
    const header = { 'content-type': 'application/json' };
    if (token) {
      const token = wx.getStorageSync('token');
      if (token) header['Authorization'] = 'Bearer ' + token;
    }
    wx.request({
      url: BASE_URL + url,
      method: method,
      data: data,
      header: header,
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 0) {
            resolve(res.data.data);
          } else {
            wx.showToast({ title: res.data.message, icon: 'none' });
            reject(res.data);
          }
        } else {
          wx.showToast({ title: '网络错误', icon: 'none' });
          reject(res);
        }
      },
      fail: (err) => {
        wx.showToast({ title: '请求失败', icon: 'none' });
        reject(err);
      }
    });
  });
};

module.exports = {
  get: (url, data, token) => request(url, 'GET', data, token),
  post: (url, data, token) => request(url, 'POST', data, token),
  put: (url, data, token) => request(url, 'PUT', data, token),
  del: (url, data, token) => request(url, 'DELETE', data, token)
};