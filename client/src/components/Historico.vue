
<template>
  <v-content>
    <v-row>
      <v-col class="c-painelHeader">
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels mobile-breakpoint="400" active-class="c-painelAtivo" >
          <t-expansion-panel :key="item.id" v-for="item in historicos" :historico="item"></t-expansion-panel>
        </v-expansion-panels>
      </v-col>
    </v-row>
  </v-content>
</template>

<style scoped>

.c-painelHeader{
  padding: 1px
}
</style>

<script>
import expansionPanel from "./Templates/expansion-panel";
import config from "../assets/dados/config";
import axios from "axios";
export default {
  data() {
    return {
      config : config,
      historicos: [],
      idUsuario: 2
    };
  },
  components: {
    "t-expansion-panel": expansionPanel
  },
  mounted() {
    let reqJSON = {
      usuario: JSON.parse(localStorage.getItem("USER"))
    };
    console.log("FAZ REQUISICAO BACKEND PARA POPULAR O HISTORICO");
    console.log("REQJSON: " + JSON.stringify(reqJSON));
    this.historicos = [];
    this.atualizaJson();
  },
  methods: {
    atualizaJson() {
      //Carrega itens da sala
      axios
        .get(`http://${config.api.host}:${config.api.port}/historicos/usuarios/${this.idUsuario}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            console.log(response);
            this.historicos = response.data;
          },
          error => {
            console.log(error);
          }
        );
      console.log("Requisição backend para atualizar os itens");
    }
  }
};
</script>

