<template>
  <v-content>
    <v-list class="c-nopadding">
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Novo Role'; classePopup=''"
      >
        <v-card>
          <v-img class="white--text" height="29vh" src="@/assets/img/novo_role.png">
            <div class="fill-height">
              <h2 class="c-home-title">Novo role</h2>
            </div>
          </v-img>
        </v-card>
      </v-list-item>
      <v-list-item
        three-line
        class="c-nopadding"
        @click="dialogSala = true; dialogType = 'Entrar em um Role'; classePopup='c-login-cod'"
      >
        <v-card>
          <v-img class="white--text" height="29vh" src="@/assets/img/entrar_role.png">
            <div class="fill-height">
              <h2 class="c-home-title">Entrar em um role</h2>
            </div>
          </v-img>
        </v-card>
      </v-list-item>

      <router-link to="Ranking" class="c-decoration-none">
        <v-list-item three-line class="c-nopadding">
          <v-card>
            <v-img class="white--text" height="30vh" src="@/assets/img/ranking_role.png">
              <div class="fill-height">
                <h2 class="c-home-title">Ranking</h2>
              </div>
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
          <span>{{ alertaErroMsg }}</span>
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
import axios from "axios";

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

        console.log(this.inputDialog + "");
        let salaNome = this.inputDialog + "";
        axios
          .post(
            `http://${config.api.host}${config.api.port}/salas`,
            { id: this.usuarioLogado.id },
            { auth: config.api.auth }
          )
          .then(
            response => {
              console.log(response.data);
              let salaId = response.data.id;
              axios
                .put(
                  `http://${config.api.host}${config.api.port}/salas/${salaId}/nome`,
                  salaNome,
                  {
                    headers: { "Content-Type": "text/plain" },
                    auth: config.api.auth
                  }
                )
                .then(
                  response => {
                    this.$router.push("Lobby");
                  },
                  error => {
                    console.log(error);
                  }
                );
            },
            error => {
              if (
                error.response.status == 400 &&
                error.response.data.mensagemUsuario.includes(
                  "em duas salas ao mesmo tempo"
                )
              ) {
                this.buscaSalaAtual();
              } else {
                console.log(error);
                this.alertaErro("Erro Ao criar o Role!", error.response.status);
              }
            }
          );
      } else if (this.dialogType.includes("Entrar em um Role")) {
        if (this.inputDialog.length != 4) {
          this.Erros.fieldMsg = "O codigo do role deve possuir 4 digitos";
          return;
        }
        this.Erros.fieldMsg = "";

        this.entrarSala(this.inputDialog);
      }
    },
    buscaSalaAtual() {
      axios
        .get(
          `http://${config.api.host}${config.api.port}/historicos/usuarios/${this.usuarioLogado.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            if (response.data) {
              let salaAtual = response.data.filter(obj => {
                return !obj.data_saida;
              })[0].sala;
              console.log(salaAtual);
              this.alertaErro(
                "Voce já esta em um role! Nome do role: " + salaAtual.nome
              );
            }
          },
          error => {
            console.log(error);
          }
        );
    },
    entrarSala(codSala) {
      console.log(codSala);
      axios
        .post(
          `http://${config.api.host}${config.api.port}/salas/${codSala}/${this.usuarioLogado.id}`,
          {},
          { auth: config.api.auth }
        )
        .then(
          response => {
            console.log(response.data);
          },
          error => {
            console.log(error.data);
          }
        );
    }
  },
  data() {
    return {
      config: config,
      dialogSala: false,
      dialogErro: false,
      inputDialog: "",
      dialogType: "",
      alertaErroMsg: "Erro",
      classePopup: "",
      usuarioLogado: JSON.parse(localStorage.getItem("User")),
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
  justify-content: right !important;
}
.c-decoration-none {
  text-decoration: none;
}
.c-home-title {
  width: 100%;
  text-align: right;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  -webkit-transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  padding-right: 20px;
  text-shadow: 0 0 5px #000;
}
</style>