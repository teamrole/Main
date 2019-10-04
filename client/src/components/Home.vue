<template>
  <v-content>
    <v-list class="c-nopadding">
      <v-list-item three-line class="c-nopadding">
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/novo_role.png">
            <v-card-title class="fill-height c-right">Novo role</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>
      <v-list-item three-line class="c-nopadding">
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/historico_role.png">
            <v-card-title class="fill-height c-right">Historico</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>
      <v-list-item three-line class="c-nopadding">
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/ranking_role.png">
            <v-card-title class="fill-height c-right">Ranking</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>
    </v-list>
  </v-content>
</template>

<style scoped>
.c-nopadding {
  padding: 0 !important;
}
.c-right {
  justify-content: right;
}
</style>