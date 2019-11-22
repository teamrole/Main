<template>
  <v-content>
    <v-list class="c-nopadding">
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Novo Role'; classePopup=''"
      >
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/img/novo_role.png">
            <v-card-title class="fill-height c-right">Novo role</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Entrar em um Role'; classePopup='c-login-cod'"
      >
        <v-card>
          <v-img class="white--text" height="30vh" src="@/assets/img/entrar_role.png">
            <v-card-title class="fill-height c-right">Entrar em um role</v-card-title>
          </v-img>
        </v-card>
      </v-list-item>

      <router-link to="Ranking" class="c-decoration-none">
        <v-list-item three-line class="c-nopadding">
          <v-card>
            <v-img class="white--text" height="30vh" src="@/assets/img/ranking_role.png">
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
                  @keyup="verificaCampoKeyUp()"
                  :error-messages="Erros.fieldMsg"
                  :class="classePopup"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="red darken1"
            text
            @click="dialogSala = false;inputDialog='';Erros.fieldMsg=''"
          >Cancelar</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="green darken1" text @click="confirmaSala()">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogErro" persistent max-width="290">
      <v-card>
        <v-card-title>Erro!</v-card-title>
        <v-card-text>
          <span>{{alertaErroMsg}}</span>
        </v-card-text>
        <v-card-actions>
          <v-btn color="green darken1" text @click="dialogErro = false">Ok</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template>

<script>
import config from "../assets/dados/config";

export default {
  methods: {
    alertaErro(msg) {
      this.alertaErroMsg = msg;
      this.dialogErro = true;
    },
    verificaCampoKeyUp() {
      if (this.dialogType.includes("Novo Role")) {
        if (!(this.inputDialog.length < 4 || this.inputDialog.length > 20)) {
          this.Erros.fieldMsg = "";
        }
      } else if (this.dialogType.includes("Entrar em um Role")) {
        this.inputDialog = this.inputDialog.replace(/ /g, "");
        if (this.inputDialog.length > 4) {
          this.inputDialog = this.inputDialog.substr(0, 4);
          this.Erros.fieldMsg = "";
        }
      }
    },
    confirmaSala() {
      if (this.dialogType.includes("Novo Role")) {
        if (this.inputDialog.length < 4 || this.inputDialog.length > 20) {
          this.Erros.fieldMsg =
            "O nome do role deve possuir entre 4 e 20 letras";
          return;
        }
        this.Erros.fieldMsg = "";

        let reqJSON = {
          usuario: JSON.parse(localStorage.getItem("USER")),
          nomeDoRole: this.inputDialog
        };

        console.log("REQUISICAO BACK PARA CRIAR A SALA");
        console.log("REQJSON: " + JSON.stringify(reqJSON));

        //IF RETORNO É OK, REDIRECIONA PARA A TELA DE ROLE

        if (this.inputDialog == "ROLE") {
          this.$router.push("Lobby");
        } else {
          this.alertaErro("Erro Ao criar o Role");
        }
      } else if (this.dialogType.includes("Entrar em um Role")) {
        if (this.inputDialog.length != 4) {
          this.Erros.fieldMsg = "O codigo do role deve possuir 4 digitos";
          return;
        }
        this.Erros.fieldMsg = "";

        let reqJSON = {
          usuario: JSON.parse(localStorage.getItem("USER")),
          codigoDoRole: this.inputDialog
        };

        console.log("REQUISICAO BACK PARA ENTRAR NA SALA");
        console.log("REQJSON: " + JSON.stringify(reqJSON));

        //IF RETORNO É OK, REDIRECIONA PARA A TELA DE ROLE
        if (this.inputDialog == "6669") {
          this.$router.push("Lobby");
        } else {
          this.Erros.fieldMsg = "O codigo do role é invalido";
        }
      }
    }
  },
  data() {
    return {
      config : config,
      dialogSala: false,
      dialogErro: false,
      inputDialog: "",
      dialogType: "",
      alertaErroMsg: "Erro",
      classePopup: "",
      Erros: {
        fieldMsg: ""
      }
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
.c-decoration-none {
  text-decoration: none;
}
</style>