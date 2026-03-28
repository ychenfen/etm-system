import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    token: localStorage.getItem('token') || '',
    collapse: false
  },
  mutations: {
    SET_USER(state, user) {
      state.userInfo = user
      localStorage.setItem('userInfo', JSON.stringify(user))
    },
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    LOGOUT(state) {
      state.userInfo = {}
      state.token = ''
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
    },
    TOGGLE_COLLAPSE(state) {
      state.collapse = !state.collapse
    }
  },
  actions: {
    login({ commit }, userData) {
      commit('SET_TOKEN', userData.token)
      commit('SET_USER', userData)
    },
    logout({ commit }) {
      commit('LOGOUT')
    }
  }
})
