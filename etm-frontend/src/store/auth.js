import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/utils/request'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('etm_token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('etm_user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const roles = computed(() => userInfo.value?.roles || [])
  const username = computed(() => userInfo.value?.username || '')
  const realName = computed(() => userInfo.value?.realName || '')

  function hasRole(role) {
    return roles.value.includes(role)
  }

  function isAdmin() {
    return hasRole('admin')
  }

  function isHR() {
    return hasRole('hr') || hasRole('admin')
  }

  async function login(username, password) {
    const res = await request.post('/auth/login', { username, password })
    const { accessToken } = res.data
    token.value = accessToken
    localStorage.setItem('etm_token', accessToken)
    await fetchUserInfo()
    return res
  }

  async function fetchUserInfo() {
    const res = await request.get('/auth/info')
    userInfo.value = res.data
    localStorage.setItem('etm_user', JSON.stringify(res.data))
    return res.data
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('etm_token')
    localStorage.removeItem('etm_user')
  }

  return {
    token, userInfo, isLoggedIn, roles, username, realName,
    hasRole, isAdmin, isHR, login, fetchUserInfo, logout
  }
})
