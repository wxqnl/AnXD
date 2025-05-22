import { createRouter, createWebHashHistory } from 'vue-router'
import store from '../store'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../pages/Login.vue'),
    meta: { title: '登录', requireAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../pages/Layout.vue'),
    redirect: '/dashboard',
    meta: { requireAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../pages/Dashboard.vue'),
        meta: { title: '欢迎使用', requireAuth: true }
      },
      {
        path: '/sysuser',
        name: 'SysUser',
        component: () => import('../pages/system/SysUser.vue'),
        meta: { title: '用户管理', requireAuth: true }
      },
      {
        path: '/sysrole',
        name: 'SysRole',
        component: () => import('../pages/system/SysRole.vue'),
        meta: { title: '角色管理', requireAuth: true }
      },
      {
        path: '/sysdept',
        name: 'SysDept',
        component: () => import('../pages/system/SysDept.vue'),
        meta: { title: '部门管理', requireAuth: true }
      },
      {
        path: '/syspost',
        name: 'SysPost',
        component: () => import('../pages/system/SysPost.vue'),
        meta: { title: '岗位管理', requireAuth: true }
      },
      {
        path: '/driver',
        name: 'Driver',
        component: () => import('../pages/driver/DriverInfo.vue'),
        meta: { title: '司机管理', requireAuth: true }
      },
      {
        path: '/customer',
        name: 'Customer',
        component: () => import('../pages/customer/CustomerInfo.vue'),
        meta: { title: '客户管理', requireAuth: true }
      },
      {
        path: '/order',
        name: 'Order',
        component: () => import('../pages/order/OrderInfo.vue'),
        meta: { title: '订单管理', requireAuth: true }
      },
      {
        path: '/ordercomment',
        name: 'OrderComment',
        component: () => import('../pages/order/OrderComment.vue'),
        meta: { title: '订单评价', requireAuth: true }
      },
      {
        path: '/coupon',
        name: 'Coupon',
        component: () => import('../pages/coupon/Coupon.vue'),
        meta: { title: '优惠券管理', requireAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 代驾管理系统` : '代驾管理系统'
  
  // 判断是否需要登录权限
  if (to.meta.requireAuth) {
    const token = store.state.user.token
    
    if (token) {
      next()
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } else {
    next()
  }
})

export default router 