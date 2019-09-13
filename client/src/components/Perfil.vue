
<template>
  <v-content>
    <v-row class="c-con-display">
      <v-col cols="12" class="c-margin-auto">
        <v-avatar size="120">
          <img src="../assets/Perfil/perfil5.jpg" />
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

    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="9">
        <v-text-field label="Ana Banana" :disabled="true"
        color='#033'></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="9">
        <v-text-field label="+55 (43)9965-0090" class="c-text-field" :disabled="true"></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row class="btn-align">
      <v-col cols="6" class="c-margin-auto">
        <v-btn color="error" dark large :outlined="true" width="75%">Sair</v-btn>
      </v-col>
      <v-col cols="6" class="c-margin-auto">
        <v-btn color="error" dark large width="75%">Apagar Conta</v-btn>
      </v-col>
    </v-row>
  </v-content>
</template>
<style scoped>
.c-text-field {
  padding-top: 0;
}
.btn-align {
  text-align: right;
}
.c-con-display {
  background: #9c27b0;
}
.c-margin-auto {
  text-align: center;
  padding-bottom: 0;
}

.c-col1 {
  text-align: center;
  padding-bottom: 0;
}
.c-col2 {
  text-align: center;
  padding-bottom: 0;
  padding-top: 0px;
}
.c-profileInfo {
  padding-top: 30px;
}
</style>