<template>
  <v-expansion-panel v-for="(item,i) in 15" :key="i">
    <v-expansion-panel-header class="c-painelHeader" @click="headerClick">
      <div class="col-9">
        <span class="c-primary title">Boteco do Ceará</span>
      </div>
      <div class="col-3">
        <span class="c-primary caption c-data">27/08/2019</span>
      </div>
    </v-expansion-panel-header>
    <v-expansion-panel-content>
      <div class="col-12 d-inline-flex">
        <div class="col-4 text-center">
          <span class="text-right font-weight-bold">
            <v-icon color="orange darken-2">calendar_today</v-icon>27/08/2019
          </span>
        </div>
        <div class="col-4 text-center">
          <v-icon color="orange darken-2">group</v-icon>
          <span class="font-weight-bold">4</span>
        </div>
        <div class="col-4 text-center">
          <span class="text-center font-weight-bold">
            <v-icon color="orange darken-2">monetization_on</v-icon>57,39
          </span>
        </div>
      </div>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>