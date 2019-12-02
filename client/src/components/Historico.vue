
<template>
  <v-content>
    <v-row class="c-crow">
      <v-col class="c-painelHeader">
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels mobile-breakpoint="400" active-class="c-painelAtivo">
          <t-expansion-panel :key="item.id" v-for="item in historicos" :historico="item"></t-expansion-panel>
        </v-expansion-panels>
      </v-col>
    </v-row>

    <v-dialog v-model="dialogLoading" fullscreen full-width>
      <v-container fluid fill-height style="background-color: rgba(255, 255, 255, 0.5);">
        <v-layout justify-center align-center>
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
        </v-layout>
      </v-container>
    </v-dialog>
  </v-content>
</template>

<style scoped>
.c-painelHeader {
  padding: 1px;
}
.c-crow{
  padding-right: 0px;
  margin-right: 0px;
}
</style>

<script>
import expansionPanel from "./Templates/panel-historico";
import config from "../assets/dados/config";

import axios from "axios";
export default {
  data() {
    return {
      config: config,
      historicos: [],
      dialogLoading: false,
      usuarioLogado: JSON.parse(localStorage.getItem("User"))
    };
  },
  components: {
    "t-expansion-panel": expansionPanel
  },
  mounted() {
    let reqJSON = {
      usuario: JSON.parse(localStorage.getItem("USER"))
    };

    this.historicos = [];
    this.atualizaJson();
  },
  methods: {
    atualizaJson() {
      this.dialogLoading = true;
      axios
        .get(`${config.api.url}/historicos/usuarios/${this.usuarioLogado.perfil.id}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            this.dialogLoading = false;
            this.historicos = response.data;
          },
          error => {
            this.dialogLoading = false;
            console.log(error);
          }
        );
    }
  }
};
</script>

