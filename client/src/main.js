import Vue from 'vue'
import App from './App.vue'
import './assets/css/style.css'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource';
import { routes } from './routes';

Vue.use(VueRouter);

const router = new VueRouter({
  routes: routes
})

new Vue({
  el: '#app',
  router: router, 
  render: h => h(App)
})
