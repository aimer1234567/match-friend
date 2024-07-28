import { createRouter, createWebHistory } from 'vue-router'
import app from "@/App.vue"
import user from "@/views/user.vue"
import apply from "@/views/apply.vue"
import index from "@/views/index.vue"
import edit from "@/views/userEdit.vue"
import login from '@/views/login.vue';
import BasicLayoutsVue from '../layouts/BasicLayouts.vue'
import  register from '@/views/register.vue'
import userSpace from '@/views/userSpace.vue'
import addFriend from '@/views/addFriend.vue'
import about from '@/views/about.vue'
import article from '@/views/article.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect:'/index',
      component: BasicLayoutsVue,
      children: [
        {
          path: 'user',
          name: 'user',
          component: user,
          meta:{
            keepAlive:false
          }
        },
        {
          path: 'edit',
          name: 'userEdit',
          component: edit,
          meta:{
            keepAlive:false
          }
        },
        {
          path: 'apply',
          name: 'apply',
          component: apply,
          meta:{
            keepAlive:true
          }
        },
        {
          path: 'index',
          name: 'index',
          component: index,
          meta:{
            keepAlive:true
          }
        },
        {
          path:'userSpace/:userAccount',
          name:'userSpace',
          component:userSpace,
          meta:{
            keepAlive:false
          }
        },{
          path:'about',
          name:'about',
          component:about,
          meta:{
            keepAlive:false
          }
        },
        {
          path:'addFriend/:userAccount',
          name:'addFriend',
          component:addFriend,
          meta:{
            keepAlive:false
          }
        },
        {
          path:'article.vue',
          name:'article',
          component:article,
          meta:{
            keepAlive:false
          }
        }
      ]
    },
    {
      path:'/login',
      name:'login',
      component:login
    },
    {
      path:'/register',
      name:'register',
      component:register
    }
  ]
})
const whiteList = ['/login','/register']
router.beforeEach((to,from,next)=>{
  let token=localStorage.getItem('token')
  if(token){
    if(to.path==='/login'){
      next('/index')
      }else{
        next()
      }
    } else { // 没有 token
      // 是否去白名单 ？
      if (whiteList.includes(to.path)) { // 去白名单
        next() // 放行
      } else { // 不去白名单
        next('/login') // 去登录页
      }
    } 
  }
)

export default router
