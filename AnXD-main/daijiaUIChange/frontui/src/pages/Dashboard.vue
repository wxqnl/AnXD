<template>
  <div class="welcome-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>欢迎使用代驾管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <el-row>
          <el-col :span="24">
            <div class="welcome-title">
              <el-icon class="welcome-icon"><Service /></el-icon>
              <h2>代驾管理系统</h2>
            </div>
            <div class="welcome-desc">
              <p>请使用左侧菜单进行管理操作</p>
            </div>
            <div class="feature-list">
              <h3>系统功能</h3>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-card class="feature-card">
                    <template #header>
                      <div class="feature-header">
                        <el-icon><Avatar /></el-icon>
                        <span>用户管理</span>
                      </div>
                    </template>
                    <p>管理司机和客户信息</p>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card class="feature-card">
                    <template #header>
                      <div class="feature-header">
                        <el-icon><ShoppingCart /></el-icon>
                        <span>订单管理</span>
                      </div>
                    </template>
                    <p>查看和管理代驾订单</p>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card class="feature-card">
                    <template #header>
                      <div class="feature-header">
                        <el-icon><Setting /></el-icon>
                        <span>系统设置</span>
                      </div>
                    </template>
                    <p>配置系统用户和权限</p>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Service, Avatar, ShoppingCart, Setting } from '@element-plus/icons-vue'
</script>

<style scoped>
.welcome-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 20px 0;
}

.welcome-title {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.welcome-icon {
  font-size: 32px;
  margin-right: 10px;
  color: #409EFF;
}

.welcome-desc {
  margin-bottom: 30px;
  color: #606266;
  font-size: 16px;
}

.feature-list {
  margin-top: 30px;
}

.feature-list h3 {
  margin-bottom: 20px;
  font-weight: 500;
  color: #303133;
}

.feature-card {
  height: 150px;
  margin-bottom: 20px;
}

.feature-header {
  display: flex;
  align-items: center;
}

.feature-header .el-icon {
  margin-right: 8px;
  color: #409EFF;
}
</style> 