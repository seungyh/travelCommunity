import './assets/main.css'
import './assets/header.css'

import './components/fontawesome/FontAwesome'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

const app = createApp(App)

// 전역 컴포넌트 등록
app.component('font-awesome-icon', FontAwesomeIcon)

app.use(router).mount('#app')
