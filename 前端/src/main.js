import './assets/main.css'
// 1. 引入你需要的组件
import Vant from 'vant'
// 2. 引入组件样式
import 'vant/lib/index.css';
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import NutUI from "@nutui/nutui";
import "@nutui/nutui/dist/style.css";
import store from './store';



const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(Vant)
app.use(NutUI)
app.use(store)
app.mount('#app')

