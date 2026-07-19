import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/UserLogin.vue'
import Home from '../views/UserHome.vue'
import VenueManage from '../views/VenueManage.vue'
import MatchManage from '../views/MatchManage.vue'
import OrderManage from '../views/OrderManage.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/login', component: Login },
  { 
    path: '/', 
    component: Home,
    meta: { requiresAuth: true },
    children: [
      { path: 'venues', component: VenueManage },
      { path: 'matches', component: MatchManage },
      { path: 'orders', component: OrderManage },
      { path: '', redirect: 'venues' }
    ]
  },
  { path: '/orders', component: OrderManage, meta: { requiresAuth: true } }
]

const router = new VueRouter({ routes })

// 路由守卫（如果需要）
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('adminToken')
    if (!token) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router