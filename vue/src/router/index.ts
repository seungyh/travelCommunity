import { createRouter, createWebHistory } from 'vue-router'

// 페이지 import
import MainPage from '../pages/home/MainPage.vue'
import DefaultLayout from '@/pages/layout/DefaultLayout.vue'
import LoginPage from '@/pages/login/LoginPage.vue'

const routes = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      {
        path: '', name: 'home', component: MainPage
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router