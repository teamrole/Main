<template>
  <div>
    <v-navigation-drawer  v-if="['Login'].indexOf($route.name) <= -1" v-model="drawer" app>
      <v-list dense>

        <t-item-navbar  text="Home" icon="home"/>
        <t-item-navbar  text="Lobby" icon="view_list"/>
        <t-item-navbar  text="Ranking" icon="poll"/>
        <t-item-navbar  text="Perfil" icon="person"/>
        
      </v-list>
    </v-navigation-drawer>
    <v-app-bar app color="#8c2e86" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"  v-if="['Login'].indexOf($route.name) <= -1"></v-app-bar-nav-icon>
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