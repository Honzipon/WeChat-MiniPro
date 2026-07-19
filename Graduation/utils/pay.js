const request = require('./request.js');

const requestPayment = (orderNo) => {
  return request.post('/order/pay', { orderNo }, true).then(payParams => {
    return new Promise((resolve, reject) => {
      wx.requestPayment({
        timeStamp: payParams.timeStamp,
        nonceStr: payParams.nonceStr,
        package: payParams.package,
        signType: payParams.signType,
        paySign: payParams.paySign,
        success: resolve,
        fail: reject
      });
    });
  });
};

module.exports = { requestPayment };