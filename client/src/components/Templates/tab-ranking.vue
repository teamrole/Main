  
<template>
  <v-container>
    <v-list>
      <v-list-item v-for="item in items" :key="item.nome" @click.stop>
        <v-list-item-avatar>
          <v-img :src="item.avatar"></v-img>
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title v-text="item.nome"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <span class="qtd-role">{{item.roles}} Roles</span>
        </v-list-item-icon>
      </v-list-item>
    </v-list>
  </v-container>
</template>

<style scoped>
.qtd-role {
  color: var(--v-accent-base);
}
</style>

<script>
export default {
  props:{
    ordenar: {
      type: String,
      default: "Semanal"
    }
  },
  data: () => ({
    items: [
      {
        nome: "Izzabely Freitas",
        avatar: require("../../assets/Perfil/Perfil1.jpg"),
        roles: 8
      },
      {
        nome: "Fernanda Sales",
        avatar: require("../../assets/Perfil/Perfil2.jpg"),
        roles: 8
      },
      {
        nome: "Leticia Shikarim",
        avatar: require("../../assets/Perfil/Perfil3.jpg"),
        roles: 8
      },
      {
        nome: "Yuri Kann",
        avatar: require("../../assets/Perfil/Perfil4.jpg"),
        roles: 8
      }
    ]
  })
};
</script>
