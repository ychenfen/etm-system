import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/components/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue')
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'el-icon-s-home' }
      },
      {
        path: 'teacher',
        name: 'Teacher',
        component: () => import('@/views/teacher/index.vue'),
        meta: { title: '教师管理', icon: 'el-icon-user', roles: ['ADMIN', 'DEPARTMENT'] }
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('@/views/department/index.vue'),
        meta: { title: '院系管理', icon: 'el-icon-office-building', roles: ['ADMIN'] }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('@/views/course/index.vue'),
        meta: { title: '课程管理', icon: 'el-icon-notebook-2' }
      },
      {
        path: 'attendance',
        name: 'Attendance',
        component: () => import('@/views/attendance/index.vue'),
        meta: { title: '考勤管理', icon: 'el-icon-date' }
      },
      {
        path: 'salary',
        name: 'Salary',
        component: () => import('@/views/salary/index.vue'),
        meta: { title: '薪酬管理', icon: 'el-icon-money', roles: ['ADMIN', 'DEPARTMENT'] }
      },
      {
        path: 'evaluation',
        name: 'Evaluation',
        component: () => import('@/views/evaluation/index.vue'),
        meta: { title: '教学评价', icon: 'el-icon-star-off' }
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/notice/index.vue'),
        meta: { title: '通知公告', icon: 'el-icon-bell' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', icon: 'el-icon-s-custom', roles: ['ADMIN'] }
      },
      {
        path: 'my-attendance',
        name: 'MyAttendance',
        component: () => import('@/views/my-attendance/index.vue'),
        meta: { title: '我的考勤', icon: 'el-icon-date', roles: ['TEACHER'] }
      },
      {
        path: 'my-salary',
        name: 'MySalary',
        component: () => import('@/views/my-salary/index.vue'),
        meta: { title: '我的薪酬', icon: 'el-icon-money', roles: ['TEACHER'] }
      },
      {
        path: 'my-evaluation',
        name: 'MyEvaluation',
        component: () => import('@/views/my-evaluation/index.vue'),
        meta: { title: '我的评价', icon: 'el-icon-star-off', roles: ['TEACHER'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'el-icon-user', hidden: true }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    const token = localStorage.getItem('token')
    if (!token) {
      next('/login')
    } else {
      // 角色权限检查
      const roles = to.meta && to.meta.roles
      if (roles && roles.length > 0) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        if (roles.includes(userInfo.role)) {
          next()
        } else {
          next('/dashboard')
        }
      } else {
        next()
      }
    }
  }
})

export default router
