
<template>
  <v-content>
    <v-row>
      <v-col>
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels mobile-breakpoint="400" active-class="c-painelAtivo">
          <t-expansion-panel :key="item.id" v-for="item in historicos" :historico="item"></t-expansion-panel>
        </v-expansion-panels>
      </v-col>
    </v-row>
  </v-content>
</template>
<script>
import expansionPanel from "./Templates/expansion-panel";
export default {
  data() {
    return {
      historicos: []
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
    this.historicos = [
      {id: 1, data: "23/09/2019", role: "Boteco do Luan", quantidadeParticipantes: 4, valorTotal: 244.67,valorParcial: 58.32},
      {id: 2, data: "23/09/2019", role: "Dogg's Londrina", quantidadeParticipantes: 2, valorTotal: 224.67,valorParcial: 77.2},
      {id: 3, data: "23/09/2019", role: "Bar do Ceará", quantidadeParticipantes: 3, valorTotal: 142.67,valorParcial: 36.28},
      {id: 4, data: "23/09/2019", role: "Garden Chopp", quantidadeParticipantes: 2, valorTotal: 177.67,valorParcial: 48.88},
      {id: 5, data: "23/09/2019", role: "Delliverya", quantidadeParticipantes: 9, valorTotal: 987.67,valorParcial: 133.76},
      {id: 6, data: "23/09/2019", role: "New York Louge", quantidadeParticipantes: 5, valorTotal: 344.67,valorParcial: 70.21},
      {id: 7, data: "23/09/2019", role: "SantArena", quantidadeParticipantes: 3, valorTotal: 198.67,valorParcial: 26.96},
      {id: 8, data: "23/09/2019", role: "Maximus bar", quantidadeParticipantes: 6, valorTotal: 394.67,valorParcial: 92.13}
    ];
  }
};
</script>

