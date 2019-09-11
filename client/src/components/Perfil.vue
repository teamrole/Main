
<template>
  <v-content>
    <v-container class="c-con-display">
      <v-row>
        <v-col cols="12" class="c-margin-auto">
          <v-avatar size="120">
            <img
              src="https://cdn.jetlaggin.com/wp-content/uploads/2017/09/26154236/dddddddddddddddddddddddddedd_15064657555076.jpg"
            />
          </v-avatar>
        </v-col>
        <v-col col="12" class="c-col">
          <v-row>
            <v-col cols="col-6" class="c-col1">
              <span class="headline">12</span>
            </v-col>
            <v-col cols="col-6" class="c-col1">
              <span class="headline">R$ 534,27</span>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="col-6" class="c-col2">
              <span class="overline">Total de Rolês</span>
            </v-col>
            <v-col cols="col-6" class="c-col2">
              <span class="overline">Total Pago</span>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </v-content>
</template>
<style scoped>
.c-con-display {
  background: #9c27b0;
  padding-bottom: 0;
}
.c-margin-auto {
  text-align: center;
  padding-bottom: 0;
}
.c-col{
  padding: 0;
}
.c-col1 {
  text-align: center;
  margin: 0;
  padding-bottom: 0;
}
.c-col2 {
  text-align: center;
  padding-bottom: 0;
  padding-top:0px;
}
</style>