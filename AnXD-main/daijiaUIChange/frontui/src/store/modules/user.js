import { login, getUserInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

const state = {
  token: getToken(),
  userInfo: null
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
  },
  CLEAR_USER(state) {
    state.token = ''
    state.userInfo = null
    removeToken()
  }
}

const actions = {
  // 登录
  async login({ commit }, userInfo) {
    try {
      const { data } = await login(userInfo)
      commit('SET_TOKEN', data)
      setToken(data)
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  },
  
  // 获取用户信息
  async getUserInfo({ commit, state }) {
    try {
      const { data } = await getUserInfo()
      commit('SET_USER_INFO', data)
      return Promise.resolve(data)
    } catch (error) {
      return Promise.reject(error)
    }
  },
  
  // 退出登录
  logout({ commit }) {
    commit('CLEAR_USER')
    return Promise.resolve()
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 