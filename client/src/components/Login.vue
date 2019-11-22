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
          ></v-text-field>
          <v-btn
            class="c-btn-margin"
            color="primary"
            href="#"
            elevation="3"
            width="100%"
            :large="true"
            @click="verificaTel()"
          >Enviar</v-btn>
        </v-col>
      </v-row>
    </v-container>

    <v-dialog v-model="dialogCod" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <!-- <span class="headline">Insira o codigo recebido por SMS</span> -->
          <span class="headline">Digite {{statusSenha}} senha:</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  label="Senha"
                  v-model="codigoAut"
                  :error-messages="Erros.fieldCodMsg"
                  type="password"
                  class="c-login-cod"
                  @keyup="/*verificaCodKeyUp() Apenas para SMS*/"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="dialogCod = false; codigoAut = ''">Cancelar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="verificaCod()">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
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
    verificaCodKeyUp() {
      this.codigoAut = this.codigoAut.replace(/ /g, "");
      if (this.codigoAut.length > 6) {
        this.codigoAut = this.codigoAut.substr(0, 6);
        this.Erros.fieldCodMsg = "";
      }
    },
    verificaCod() {
      if (this.codigoAut.length < 6) {
        this.Erros.fieldCodMsg = "Sua senha deve possuir no minimo 6 Digitos";
      } else {
        return;
        this.Erros.fieldCodMsg = "";
        axios
          .post(
            `http://${config.api.host}:${config.api.port}/usuarios`,
            {
              celular: this.userTel
                .replace("+55 (", "")
                .replace(") ", "")
                .replace("-", ""),
              permissao: [],
              senha: this.codigoAut
            },
            {
              auth: config.api.auth
            }
          )
          .then(
            response => {
              axios
                .post(
                  `http://${config.api.host}:${config.api.port}/perfis`,
                  {
                    usuario: {
                      id: response.data.id
                    }
                  },
                  {
                    auth: config.api.auth
                  }
                )
                .then(
                  response => {
                    axios
                      .get(
                        `http://${config.api.host}:${config.api.port}/usuarios/${response.data.id}`,

                        {
                          auth: config.api.auth
                        }
                      )
                      .then(
                        response => {
                          console.log(response.data);
                          localStorage.setItem(
                            "USER",
                            JSON.stringify(response.data)
                          );
                          this.$router.push("Perfil");
                        },
                        error => {
                          console.log(error);
                        }
                      );
                  },
                  error => {
                    console.log(error);
                  }
                );
            },
            error => {
              console.log(error);
            }
          );
      }
    },
    verificaTel() {
      let telSemMask = this.unmaskTel(this.userTel);
      console.log(telSemMask);
      if (telSemMask.length < 10) {
        this.Erros.fieldTelMsg = "O Telefone deve possuir 10 ou 11 Digitos";
      } else {
        this.Erros.fieldTelMsg = "";

        axios
          .get(`http://${config.api.host}:${config.api.port}/usuarios`, {
            auth: config.api.auth
          })
          .then(
            response => {
              let flag = false;
              response.data.some(function(a) {
                if (a.celular == telSemMask) {
                  flag = true;
                  return;
                }
              });
              this.statusSenha = flag ? "sua" : "uma nova";
              this.dialogCod = true;
            },
            error => {}
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
    codigoAut: "",
    dialogCod: false,
    statusSenha: "sua",
    Erros: {
      fieldTelMsg: "",
      fieldCodMsg: ""
    }
  }),
  created() {
    if (localStorage.getItem("USER")) {
      let user = JSON.parse(localStorage.getItem("USER"));
      console.log(
        user.telefone + " É O USUARIO LOGADO, GET PARA VALIDAR BACKEND"
      );
      //Revalida autenticação com backend
      //Caso revalidação OK redireciona para Home
      //Caso não, segue fluxo como novo usuario
    } else {
      console.log("NOVO USUARIO");
    }
  }
};
</script>