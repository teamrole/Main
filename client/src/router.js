import Vue from 'vue'
import Router from 'vue-router'
import Home from './components/Home'
import Login from './components/Login'
import Ranking from './components/Ranking'
import Historico from './components/Historico'
import Perfil from './components/Perfil'

Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Ranking',
      name: 'Ranking',
      component: Ranking
    },
    {
      path: '/Historico',
      name: 'Historico',
      component: Historico
    },
    {
      path: '/Perfil',
      name: 'Perfil',
      component: Perfil
      },
  ]
})
