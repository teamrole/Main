<template>
  <v-content>
    <v-container>
      <v-tabs v-model="tab" color="primary" grow>
        <v-tab v-for="item in items" :key="item.titulo" class="c-tab-header">{{ item.titulo }}</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item v-for="item in items" v-bind:key="item.titulo">
          <v-card flat>
            <v-card-text>
              <t-tab-ranking :ordenar="item.componente" :apiPath="item.path" />
            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-container>
  </v-content>
</template>

<style scoped>
.c-tab-header {
  margin: 0 !important;
}
</style>

<script>
import tabranking from "./Templates/tab-ranking";
import config from "../assets/dados/config";

export default {
  components: {
    "t-tab-ranking": tabranking
  },
  data() {
    const today = new Date();
    today.setTime(today.getTime() + 3 * 60 * 60 * 1000);
    const date =
      today.getFullYear() +
      "-" +
      (today.getMonth() + 1) +
      "-" +
      today.getDate();
    console.log(date);
    return {
      config: config,
      tab: null,
      items: [
        {
          titulo: "Diário",
          componente: "Diario",
          path: "rankings/dia?date=" + date
        },
        {
          titulo: "Mensal",
          componente: "Mensal",
          path: "rankings/mes?date=" + date
        },
        {
          titulo: "Total",
          componente: "Mensal",
          path: "rankings"
        }
      ]
    };
  }
};
</script>

