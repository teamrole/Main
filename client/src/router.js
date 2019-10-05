import Vue from 'vue'
import Router from 'vue-router'
import Login from './components/Login'
import Ranking from './components/Ranking'
import Historico from './components/Historico'
import Lobby from './components/Lobby'
import Perfil from './components/Perfil'
import Notfound from './components/NotFound'
import Home from './components/Home'

Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
      alias: '/Home'
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
      path: '/Lobby',
      name: 'Lobby',
      component: Lobby,
    },
    {
      path: '*',
      name: 'Notfound',
      component: Notfound
    },
    {
      path: '/Perfil',
      name: 'Perfil',
      component: Perfil
    }
  ]
})
