import request from '@/utils/request'

// ========== 认证相关 ==========
export function login(data) {
  return request({ url: '/auth/login', method: 'post', data })
}
export function getUserInfo() {
  return request({ url: '/auth/info', method: 'get' })
}
export function changePassword(data) {
  return request({ url: '/auth/password', method: 'put', data })
}

// ========== 仪表盘 ==========
export function getDashboard() {
  return request({ url: '/dashboard/statistics', method: 'get' })
}

// ========== 用户管理 ==========
export function getUserPage(params) {
  return request({ url: '/user/page', method: 'get', params })
}
export function addUser(data) {
  return request({ url: '/user', method: 'post', data })
}
export function updateUser(data) {
  return request({ url: '/user', method: 'put', data })
}
export function deleteUser(id) {
  return request({ url: `/user/${id}`, method: 'delete' })
}
export function resetPassword(id) {
  return request({ url: `/user/reset-password/${id}`, method: 'put' })
}
export function changeUserStatus(id, status) {
  return request({ url: `/user/status/${id}`, method: 'put', params: { status } })
}

// ========== 院系管理 ==========
export function getDeptPage(params) {
  return request({ url: '/department/page', method: 'get', params })
}
export function getDeptList() {
  return request({ url: '/department/list', method: 'get' })
}
export function addDept(data) {
  return request({ url: '/department', method: 'post', data })
}
export function updateDept(data) {
  return request({ url: '/department', method: 'put', data })
}
export function deleteDept(id) {
  return request({ url: `/department/${id}`, method: 'delete' })
}

// ========== 教师管理 ==========
export function getTeacherPage(params) {
  return request({ url: '/teacher/page', method: 'get', params })
}
export function getTeacherList() {
  return request({ url: '/teacher/list', method: 'get' })
}
export function addTeacher(data) {
  return request({ url: '/teacher', method: 'post', data })
}
export function updateTeacher(data) {
  return request({ url: '/teacher', method: 'put', data })
}
export function deleteTeacher(id) {
  return request({ url: `/teacher/${id}`, method: 'delete' })
}
export function getTeacherById(id) {
  return request({ url: `/teacher/${id}`, method: 'get' })
}
export function approveTeacher(id) {
  return request({ url: `/teacher/approve/${id}`, method: 'put' })
}
export function rejectTeacher(id) {
  return request({ url: `/teacher/reject/${id}`, method: 'put' })
}

// ========== 课程管理 ==========
export function getCoursePage(params) {
  return request({ url: '/course/page', method: 'get', params })
}
export function addCourse(data) {
  return request({ url: '/course', method: 'post', data })
}
export function updateCourse(data) {
  return request({ url: '/course', method: 'put', data })
}
export function deleteCourse(id) {
  return request({ url: `/course/${id}`, method: 'delete' })
}
export function getCourseList(params) {
  return request({ url: '/course/list', method: 'get', params })
}

// ========== 考勤管理 ==========
export function getAttendancePage(params) {
  return request({ url: '/attendance/page', method: 'get', params })
}
export function addAttendance(data) {
  return request({ url: '/attendance', method: 'post', data })
}
export function updateAttendance(data) {
  return request({ url: '/attendance', method: 'put', data })
}
export function deleteAttendance(id) {
  return request({ url: `/attendance/${id}`, method: 'delete' })
}

// ========== 薪酬管理 ==========
export function getSalaryPage(params) {
  return request({ url: '/salary/page', method: 'get', params })
}
export function addSalary(data) {
  return request({ url: '/salary', method: 'post', data })
}
export function updateSalary(data) {
  return request({ url: '/salary', method: 'put', data })
}
export function deleteSalary(id) {
  return request({ url: `/salary/${id}`, method: 'delete' })
}
export function approveSalary(id) {
  return request({ url: `/salary/approve/${id}`, method: 'put' })
}
export function paySalary(id) {
  return request({ url: `/salary/pay/${id}`, method: 'put' })
}

// ========== 评价管理 ==========
export function getEvalPage(params) {
  return request({ url: '/evaluation/page', method: 'get', params })
}
export function addEval(data) {
  return request({ url: '/evaluation', method: 'post', data })
}
export function updateEval(data) {
  return request({ url: '/evaluation', method: 'put', data })
}
export function deleteEval(id) {
  return request({ url: `/evaluation/${id}`, method: 'delete' })
}

// ========== 数据导出 ==========
export function exportCourse() {
  return request({ url: '/export/course', method: 'get', responseType: 'blob' })
}
export function exportTeacher() {
  return request({ url: '/export/teacher', method: 'get', responseType: 'blob' })
}
export function exportAttendance() {
  return request({ url: '/export/attendance', method: 'get', responseType: 'blob' })
}
export function exportSalary() {
  return request({ url: '/export/salary', method: 'get', responseType: 'blob' })
}
export function exportEvaluation() {
  return request({ url: '/export/evaluation', method: 'get', responseType: 'blob' })
}

// ========== 通知管理 ==========
export function getNoticePage(params) {
  return request({ url: '/notice/page', method: 'get', params })
}
export function addNotice(data) {
  return request({ url: '/notice', method: 'post', data })
}
export function updateNotice(data) {
  return request({ url: '/notice', method: 'put', data })
}
export function deleteNotice(id) {
  return request({ url: `/notice/${id}`, method: 'delete' })
}
