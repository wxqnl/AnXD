<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="title">
        <h2>代驾管理系统</h2>
      </div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="User" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="Lock" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// 创建表单引用
const loginFormRef = ref(null)
// 使用store
const store = useStore()
// 使用路由
const router = useRouter()
const route = useRoute()

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 登录状态
const loading = ref(false)

// 登录处理
const handleLogin = async () => {
  // 验证表单
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        await store.dispatch('user/login', loginForm)
        await store.dispatch('user/getUserInfo')
        
        ElMessage({
          message: '登录成功',
          type: 'success'
        })
        
        // 重定向到首页或之前尝试访问的页面
        router.push(route.query.redirect || '/')
      } catch (error) {
        ElMessage.error(error.message || '登录失败，请重试')
      } finally {
        loading.value = false
      }
    } else {
      return false
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  padding: 20px 30px;
  border-radius: 8px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
}

.title h2 {
  font-weight: 600;
  color: #409EFF;
  margin: 0;
}

.login-form {
  margin-top: 30px;
}

.login-button {
  width: 100%;
}
</style> 