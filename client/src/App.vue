<template>
  <div id="app">
    <t-menu></t-menu>
    <router-view/>
  </div>
</template>

<script>
import menu from './components/Header/Menu.vue'
export default {
  name: 'App',

  components: {
    't-menu': menu
  }
}

</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
