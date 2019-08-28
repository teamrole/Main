<template>
  <div>
    <v-navigation-drawer  v-if="['Login'].indexOf($route.name) <= -1" v-model="drawer" app>
      <v-list dense>

        <t-item-navbar text="Home" icon="home" goto="Home"/>
        <t-item-navbar text="Lobby" icon="view_list" goto="Lobby"/>
        <t-item-navbar text="Ranking" icon="poll" goto="Ranking"/>
        <t-item-navbar text="Perfil" icon="person" goto="Perfil"/>
        
      </v-list>
    </v-navigation-drawer>
<<<<<<< HEAD
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"  v-if="['Login'].indexOf($route.name) <= -1"></v-app-bar-nav-icon>
=======
    <v-app-bar app color="#8c2e86" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"  v-if="['Login'].indexOf($route.name) <= -1"></v-app-bar-nav-icon>
>>>>>>> origin/EF-Login
      <v-toolbar-title>iRole</v-toolbar-title>
    </v-app-bar>
  </div>
</template>

<script>

import itemnavbar from "./item-navbar"

export default {
  data: () => ({
    drawer: null,
  }),
  components: {
    "t-item-navbar": itemnavbar
  }
}
</script>