<template>
  <v-content>
    <v-container fill-height="100" .offset-lg-4>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="4" class="c-text-right">
          <v-text-field
            v-mask="mask"
            label="Insira seu número"
            color="primary"
            :outlined="true"
            :clearable="true"
            :hide-details="!Erros.fieldTelMsg"
            placeholder="+55(99)9 9999-9999"
            v-model="userTel"
            :error-messages="Erros.fieldTelMsg"
            ref="fieldtelefone"
          ></v-text-field>
          <v-btn
            class="c-btn-margin"
            color="primary"
            href="#"
            elevation="3"
            width="100%"
            :large="true"
            @click="buscaTel()"
          >Entrar</v-btn>
        </v-col>
      </v-row>
    </v-container>

    <v-dialog v-model="dialogSenha" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <!-- <span class="headline">Insira o codigo recebido por SMS</span> -->
          <span
            class="headline"
          >{{statusSenha === "sua"? "Digite a sua senha": "Bem Vindo! Digite uma senha para se cadastrar"}}</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  label="Senha"
                  v-model="senha"
                  :error-messages="Erros.fieldCodMsg"
                  type="password"
                  class="c-login-cod"
                  ref="fieldsenha"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="dialogSenha = false; senha = ''">Cancelar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="validaUsuario()">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogLoading" fullscreen full-width>
      <v-container fluid fill-height style="background-color: rgba(255, 255, 255, 0.5);">
        <v-layout justify-center align-center>
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
        </v-layout>
      </v-container>
    </v-dialog>
  </v-content>
</template>

<style scoped>
.c-text-right {
  text-align: right;
}
.c-btn-margin {
  color: white;
  margin-top: 15px;
}
</style>
<script>
import { mask } from "vue-the-mask";
import config from "../assets/dados/config";
import axios from "axios";

export default {
  methods: {
    validaUsuario() {
      if (this.senha.length < 5) {
        this.Erros.fieldCodMsg = "Sua senha deve possuir no minimo 5 Digitos";
      } else {
        this.Erros.fieldCodMsg = "";
        if (this.statusSenha === "sua") {
          this.fazLogin(
            {
              celular: this.unmaskTel(this.userTel),
              senha: this.senha
            },
            false
          );
        } else {
          this.criaUsuario();
        }
      }
    },
    fazLogin(user, isLogado) {
      this.dialogLoading = true;
      axios
        .post(`${config.api.url}/usuarios/login`, user, {
          auth: config.api.auth
        })
        .then(
          response => {
            this.dialogLoading = false;
            if (!isLogado) {
              user.id = response.data.id;
              user.perfil = {
                id: response.data.perfil.id
              };
              localStorage.setItem("User", JSON.stringify(user));
              localStorage.setItem("firstLogin", true);
              this.$router.push("Perfil");
            } else {
              this.$router.push("Home");
            }
          },
          error => {
            this.dialogLoading = false;
            if (error.response.status == 401) {
              this.Erros.fieldCodMsg = "Senha invalida, tente novamente";
            } else {
              console.log(error);
            }
          }
        );
    },
    criaUsuario() {
      this.dialogLoading = true;
      axios
        .post(
          `${config.api.url}/usuarios`,
          {
            celular: this.unmaskTel(this.userTel),
            senha: this.senha
          },
          {
            auth: config.api.auth
          }
        )
        .then(
          response => {
            this.dialogLoading = false;
            console.log(response.data);
            localStorage.setItem(
              "User",
              JSON.stringify({
                celular: this.unmaskTel(this.userTel),
                senha: this.senha,
                id: response.data.id,
                perfil: {
                  id: response.data.perfil.id
                }
              })
            );
            localStorage.setItem("firstLogin", true);
            this.$router.push("Perfil");
          },
          error => {
            console.log(error);
          }
        );
    },
    buscaTel() {
      let telSemMask = this.unmaskTel(this.userTel);
      if (telSemMask.length < 10) {
        this.Erros.fieldTelMsg = "O Telefone deve possuir 10 ou 11 Digitos";
      } else {
        this.Erros.fieldTelMsg = "";
        this.dialogLoading = true;
        axios
          .get(`${config.api.url}/usuarios`, {
            auth: config.api.auth
          })
          .then(
            response => {
              this.dialogLoading = false;
              this.statusSenha = response.data
                .map(a => a.celular)
                .includes(telSemMask)
                ? "sua"
                : "uma nova";
              this.dialogSenha = true;
              setTimeout(() => {
                this.$refs.fieldsenha.focus();
              }, 500);
            },
            error => {
              console.error(error);
            }
          );
      }
    },
    unmaskTel(tel) {
      return tel
        .replace("+55 (", "")
        .replace(") ", "")
        .replace("-", "");
    }
  },
  directives: {
    mask
  },
  data: () => ({
    config: config,
    mask: "+55 (##) #####-####",
    userTel: "",
    senha: "",
    dialogSenha: false,
    dialogLoading: true,
    statusSenha: "sua",
    Erros: {
      fieldTelMsg: "",
      fieldCodMsg: ""
    }
  }),
  mounted() {
    if (localStorage.getItem("User")) {
      let JSONuser = JSON.parse(localStorage.getItem("User"));
      let user = {
        celular: JSONuser.celular,
        senha: JSONuser.senha
      };
      console.log(JSONuser);
      this.fazLogin(user, true);
    } else {
      console.log("NOVO USUARIO");
      this.dialogLoading = false;
      this.$refs.fieldtelefone.focus();
    }
  }
};
</script>