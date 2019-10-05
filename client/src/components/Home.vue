<template>
  <v-content>
    <v-list class="c-nopadding">
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Novo Role'"
      >
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/novo_role.png">
            <v-card-title class="fill-height c-right">Novo role</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Entrar em um Role'"
      >
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/entrar_role.png">
            <v-card-title class="fill-height c-right">Entrar em um role</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>

      <router-link to="Ranking" class="c-decoration-none">
        <v-list-item three-line class="c-nopadding">
          <v-card>
            <v-img class="white--text" height="30vh" src="@/assets/ranking_role.png">
              <v-card-title class="fill-height c-right">Ranking</v-card-title>
            </v-img>
          </v-card>
        </v-list-item>
      </router-link>
    </v-list>

    <v-dialog v-model="dialogSala" persistent max-width="290">
      <v-card>
        <v-card-title>{{dialogType}}</v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6" md="4">
                <v-text-field
                  :label="dialogType.includes('Novo Role')? 'Nome do role':'Código do role' "
                  required
                  v-model="inputDialog"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken1" text @click="dialogSala = false">Cancelar</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="green darken1" text @click="teste()">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template>

<script>
export default {
  methods: {
    teste() {
      if (this.dialogType.includes("Novo Role")) {
        console.log("Entrar na sala");
      } else if (this.dialogType.includes("Entrar em um Role")) {
        console.log("Criar sala");
      }
    }
  },
  data() {
    return {
      dialogSala: false,
      inputDialog: "",
      dialogType: ""
    };
  }
};
</script>


<style scoped>
.c-nopadding {
  padding: 0 !important;
}
.c-right {
  justify-content: right;
}
.c-decoration-none{
  text-decoration: none;
}
</style>