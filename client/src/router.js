import Vue from 'vue'
import Router from 'vue-router'
import Login from './components/Login'
import Ranking from './components/Ranking'
import Historico from './components/Historico'
import Lobby from './components/Lobby'
import Notfound from './components/NotFound'
import Home from './components/Home'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Ranking',
      name: 'ranking',
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

  ]
})
