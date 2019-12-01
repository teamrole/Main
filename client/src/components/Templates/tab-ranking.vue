  
<template>
  <v-container class="c-no-padding">
    <h1 v-if="!userData" style="text-align:center">{{mensagemVazio}}</h1>
    <v-list v-else>
      <v-list-item
        v-for="item in userData "
        :key="item.id"
        @click.stop
        :dense="true"
        class="c-no-padding"
      >
        <v-list-item-avatar>
          <v-img :src="item.foto?item.foto:fotoDefault"></v-img>
        </v-list-item-avatar>

        <v-list-item-content padding:0>
          <v-list-item-title v-text="item.nome?item.nome:'Usuario S/ Nome'"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <span class="c-qtd-role">{{item.contador}} Roles</span>
        </v-list-item-icon>
      </v-list-item>
    </v-list>

    <v-dialog v-model="dialogLoading" fullscreen full-width>
      <v-container fluid fill-height style="background-color: rgba(255, 255, 255, 0.5);">
        <v-layout justify-center align-center>
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
        </v-layout>
      </v-container>
    </v-dialog>
  </v-container>
</template>

<style scoped>
.c-qtd-role {
  color: var(--v-accent-base);
}
.c-no-padding {
  padding: 0 !important;
}
</style>

<script>
import axios from "axios";
import config from "./../../assets/dados/config";

export default {
  props: {
    apiPath: {
      type: String
    },
    ordenar: {
      type: String,
      default: "Semanal"
    }
  },
  mounted() {
    this.dialogLoading = true;
    axios
      .get(`${config.api.url}/${this.apiPath}`, {
        auth: config.api.auth
      })
      .then(
        response => {
          this.dialogLoading = false;
          console.log(response);
          this.userData = response.data;
          this.mensagemVazio = "Ainda não há Roles neste periodo";
        },
        error => {
          this.dialogLoading = false;
          console.log(error);
        }
      );
  },
  data: () => ({
    dialogLoading: false,
    items: [],
    userData: null,
    mensagemVazio: "Carregando...",
    fotoDefault:
      "https://firebasestorage.googleapis.com/v0/b/i-role.appspot.com/o/src%2Ffoto-padrao.png?alt=media&token=7899e09b-3157-4c49-a18c-66ab3f20d067"
  })
};
</script>
