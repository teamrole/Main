
<template>
  <v-content>
    <v-row>
      <v-col class="c-painelHeader">
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels mobile-breakpoint="400" active-class="c-painelAtivo">
          <t-expansion-panel :key="item.id" v-for="item in historicos" :historico="item"></t-expansion-panel>
        </v-expansion-panels>
      </v-col>
    </v-row>
  </v-content>
</template>

<style scoped>
.c-painelHeader {
  padding: 1px;
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
      axios
        .get(`${config.api.url}/historicos/usuarios/${this.usuarioLogado.id}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            this.historicos = response.data;
          },
          error => {
            console.log(error);
          }
        );
    }
  }
};
</script>

