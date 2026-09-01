import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from '@/router'


import { createPinia } from 'pinia'
import piniaPluginPersistance from 'pinia-plugin-persistedstate'
import locale from 'element-plus/dist/locale/zh-cn.js'

import App from './App.vue'


const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistance)
app.use(pinia)
app.use(router)
app.use(ElementPlus, {locale})
app.mount('#app')
