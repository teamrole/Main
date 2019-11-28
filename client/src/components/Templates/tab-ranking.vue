  
<template>
  <v-container class="c-no-padding">
    <h1 v-if="!userData" style="text-align:center">Ainda não há Roles neste periodo</h1>
    <v-list v-else>
      <v-list-item v-for="item in userData " :key="item.id" @click.stop :dense="true" class="c-no-padding">
        <v-list-item-avatar>
          <v-img :src="item.foto"></v-img>
        </v-list-item-avatar>

        <v-list-item-content padding:0>
          <v-list-item-title v-text="item.nome"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <span class="c-qtd-role">{{item.contador}} Roles</span>
        </v-list-item-icon>
      </v-list-item>
    </v-list>
  </v-container>
</template>

<style scoped>
.c-qtd-role {
  color: var(--v-accent-base);
}
.c-no-padding{
  padding: 0 !important;
}
</style>

<script>
import axios from 'axios';
import config from "./../../assets/dados/config";
export default {
  props:{
    apiPath: {
      type: String,
    },
    ordenar: {
      type: String,
      default: "Semanal"
    }
  },
  mounted(){
      
      //2019-11-27

      axios.get(`http://${config.api.host}${config.api.port}/${this.apiPath}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            console.log(response);
            this.userData = response.data;
          },
          error => {
            console.log(error);
          }
      );
    },
  data: () => ({
    items: [
      {
        nome: "Izzabely Freitas",
        avatar: "../../assets/Perfil/Perfil1.jpg",
        roles: 8
      }
    ],
    userData: null
  })
};
</script>
