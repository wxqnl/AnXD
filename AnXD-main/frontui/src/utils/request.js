import axios from 'axios'
import { ElMessage } from 'element-plus'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // url = base url + request url
  timeout: 15000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    if (store.state.user.token) {
      // 让每个请求携带token
      // 如果后端需要Bearer前缀，取消注释下面这行
      // config.headers['Authorization'] = 'Bearer ' + getToken()
      
      // 如果后端不需要Bearer前缀，使用这行
      config.headers['Authorization'] = getToken()
      
      console.log('发送请求携带token:', config.url, config.headers['Authorization'])
    } else {
      console.warn('发送请求没有token:', config.url)
    }
    
    // 记录请求的详细信息
    console.log('请求详情:', {
      url: config.url,
      method: config.method,
      params: config.params,
      data: config.data
    });
    
    return config
  },
  error => {
    // 对请求错误做些什么
    console.error('请求拦截器错误:', error) // for debug
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对于PUT请求的布尔值响应特殊处理
    if (response.config.method === 'put' && typeof response.data === 'boolean') {
      console.log('PUT请求返回布尔值:', response.data);
      return response.data;
    }
    
    // 首先检查响应数据的类型
    if (typeof response.data === 'boolean') {
      // 如果直接返回true/false（如更新用户接口）
      console.log('API响应布尔值:', response.data);
      return response.data;
    }
    
    // 如果响应不是对象，可能是字符串或其他类型
    if (typeof response.data !== 'object' || response.data === null) {
      console.log('API响应非对象值:', response.data);
      return response.data;
    }
    
    const res = response.data;
    
    // 调试信息
    console.log('API响应对象:', res);
    
    // 如果返回的状态码不是200，说明请求异常
    if (res.code !== undefined && res.code !== 200) {
      // 响应处理前，记录详细的错误信息
      console.error('请求响应异常:', {
        url: response.config.url,
        method: response.config.method,
        statusCode: res.code,
        message: res.message,
        data: res.data
      });
      
      ElMessage({
        message: res.message || '系统异常',
        type: 'error',
        duration: 5 * 1000
      });
      
      // 401: 未登录或token过期
      if (res.code === 401 || res.code === 208) {
        // 重新登录
        store.dispatch('user/logout').then(() => {
          location.reload();
        });
      }
      return Promise.reject(new Error(res.message || '系统异常'));
    } else {
      return res;
    }
  },
  error => {
    console.log('err', error); // for debug
    
    // 获取请求的URL和方法
    const url = error.config ? error.config.url : '未知URL';
    const method = error.config ? error.config.method : '未知方法';
    console.log(`请求失败: ${method.toUpperCase()} ${url}`);
    
    // 打印详细错误信息
    if (error.response) {
      console.error('响应错误详情:', {
        url: url,
        method: method,
        status: error.response.status,
        statusText: error.response.statusText,
        headers: error.response.headers,
        data: error.response.data
      });
      
      // 如果是SysUser更新失败，可能是格式问题
      if (method.toLowerCase() === 'put' && url.includes('/sysUser')) {
        console.log('分析更新用户失败原因...');
        if (error.config && error.config.data) {
          try {
            const requestData = JSON.parse(error.config.data);
            console.log('请求数据分析:', requestData);
            
            // 检查必填字段
            const missingFields = [];
            ['id', 'username', 'name', 'phone'].forEach(field => {
              if (!requestData[field]) {
                missingFields.push(field);
              }
            });
            
            if (missingFields.length > 0) {
              console.error('缺少必填字段:', missingFields);
            }
            
            // 检查数据类型
            if (requestData.id && isNaN(Number(requestData.id))) {
              console.error('ID不是有效的数字');
            }
            if (requestData.deptId && isNaN(Number(requestData.deptId))) {
              console.error('部门ID不是有效的数字');
            }
            if (requestData.postId && isNaN(Number(requestData.postId))) {
              console.error('岗位ID不是有效的数字');
            }
          } catch (e) {
            console.error('解析请求数据失败:', e);
          }
        }
      }
    } else {
      console.error('网络错误:', error.message);
    }
    
    let errorMsg = '请求失败';
    if (error.response && error.response.status) {
      switch (error.response.status) {
        case 401:
          errorMsg = '未授权，请重新登录';
          // 清除token并返回登录页
          store.dispatch('user/logout').then(() => {
            location.reload();
          });
          break;
        case 403:
          errorMsg = '拒绝访问';
          break;
        case 404:
          errorMsg = '请求的资源不存在';
          break;
        case 500:
          errorMsg = '服务器内部错误';
          if (error.response.data && error.response.data.message) {
            errorMsg += ': ' + error.response.data.message;
          } else if (method.toLowerCase() === 'put' && url.includes('/sysUser')) {
            errorMsg = '更新用户信息失败，请检查数据格式是否正确';
          }
          break;
        default:
          errorMsg = error.message || '未知错误';
      }
    }
    
    ElMessage({
      message: errorMsg,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service 