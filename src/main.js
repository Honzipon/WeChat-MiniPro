import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import router from './router'

Vue.config.productionTip = false
Vue.use(ElementUI)

// 配置 axios 基础路径
axios.defaults.baseURL = 'http://localhost:8080/api'

// 请求拦截器：自动添加 token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('adminToken');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
}, error => Promise.reject(error));

// 响应拦截器：处理 401 未授权
axios.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('adminToken');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

Vue.prototype.$http = axios;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')