<template>
  <div class="c-navbar">
    <v-navigation-drawer  v-if="['Login'].indexOf($route.name) <= -1" v-model="drawer" app>
      <v-list dense>

        <t-item-navbar text="Home" icon="home" goto="Home"/>
        <t-item-navbar text="Lobby" icon="people" goto="Lobby"/>
        <t-item-navbar text="Ranking" icon="poll" goto="Ranking"/>
        <t-item-navbar text="Historico" icon="view_list" goto="Historico"/>
        <t-item-navbar text="Perfil" icon="person" goto="Perfil"/>

      </v-list>
    </v-navigation-drawer>
    <v-app-bar app color="primary" dark class="c-navbar">
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"  v-if="['Login'].indexOf($route.name) <= -1"></v-app-bar-nav-icon>
        <v-toolbar-title v-if="['Lobby'].indexOf($route.name) <= -1">iRole</v-toolbar-title>
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