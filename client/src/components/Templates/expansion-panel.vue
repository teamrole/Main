<template>
  <v-expansion-panel>
    <v-expansion-panel-header @click.stop>
      <div class="col-7 c-painelHeader">
        <span class="title">
          <v-icon color="#9c27b0">where_to_vote</v-icon>
          {{'historico.nomeRole'}}
        </span>
      </div>
      <div>
        <span class="caption c-data">
          <v-icon color="#9c27b0">calendar_today</v-icon>
          {{ historico.data_entrada.substring(8, 10) +'/'+ historico.data_entrada.substring(5, 7) +'/'+ historico.data_entrada.substring(0, 4)}}
        </span>
      </div>
    </v-expansion-panel-header>
    <v-expansion-panel-content>
      <div class="col-12 d-inline-flex c-nopadding">
        <div class="col-4 text-center c-nopadding">
          <p class="text-left font-weight-bold">Rolezeiros:</p>
          <p class="text-left font-weight-bold">Valor Pago:</p>
          <p class="text-left font-weight-bold">Conta:</p>
        </div>
        <div class="col-4 text-center c-nopadding">
          <p class="text-center font-weight-bold">
            <v-icon color="#9c27b0">people</v-icon>
          </p>
          <p class="text-center font-weight-bold">
            <v-icon color="#9c27b0">attach_money</v-icon>
          </p>
          <p class="text-center font-weight-bold">
            <v-icon color="#9c27b0">chrome_reader_mode</v-icon>
          </p>
        </div>
        <div class="col-4 text-center c-nopadding">
          <p class="text-center font-weight-bold">{{historico.totalUsuarios}}</p> 
          <p class="text-center font-weight-bold">{{historico.totalParcial}}</p>
          <p class="text-center font-weight-bold">{{historico.totalSala}}</p>
        </div>
      </div>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>
export default {
  props: {
    historico: Object
  }
};
</script>
<style scoped>
.c-secondary {
  color: #9c27b0;
}
.c-painelAtivo > .v-expansion-panel-header,
.c-painelAtivo > .v-expansion-panel-content {
  background-color: #eee;
}
.c-painelHeader{
  padding-left: 0;
  padding-right: 0;
}
.c-nopadding{
  padding: 0;
}

</style>