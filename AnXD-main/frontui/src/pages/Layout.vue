<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="220px">
        <div class="logo">代驾管理系统</div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          router
        >
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>欢迎页面</span>
          </el-menu-item>
          
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/sysuser">用户管理</el-menu-item>
            <el-menu-item index="/sysrole">角色管理</el-menu-item>
            <el-menu-item index="/sysdept">部门管理</el-menu-item>
            <el-menu-item index="/syspost">岗位管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/driver">司机管理</el-menu-item>
            <el-menu-item index="/customer">客户管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="3">
            <template #title>
              <el-icon><ShoppingCart /></el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/order">订单列表</el-menu-item>
            <el-menu-item index="/ordercomment">订单评价</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/coupon">
            <el-icon><Discount /></el-icon>
            <span>优惠券管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header>
          <div class="header-left">
            <el-icon class="menu-toggle" @click="toggleSidebar">
              <component :is="isCollapse ? 'Expand' : 'Fold'" />
            </el-icon>
            <breadcrumb />
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :icon="UserFilled" size="small"></el-avatar>
                {{ userInfo?.nickname || userInfo?.username || '管理员' }}
                <el-icon><CaretBottom /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import Breadcrumb from '../components/Breadcrumb.vue'

const store = useStore()
const router = useRouter()
const route = useRoute()

// 获取用户信息
const userInfo = computed(() => store.state.user.userInfo)

// 侧边栏折叠状态
const isCollapse = ref(false)
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 当前激活菜单
const activeMenu = computed(() => {
  return route.path
})

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    store.dispatch('user/logout')
    router.push('/login')
  }).catch(() => {})
}

// 页面加载时获取用户信息
onMounted(() => {
  if (!userInfo.value) {
    store.dispatch('user/getUserInfo').catch(() => {
      router.push('/login')
    })
  }
})
</script>

<style scoped>
.common-layout {
  height: 100%;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.el-menu {
  border-right: none;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
}

.el-header {
  background-color: #fff;
  color: #333;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.menu-toggle {
  font-size: 20px;
  cursor: pointer;
  margin-right: 15px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style> 