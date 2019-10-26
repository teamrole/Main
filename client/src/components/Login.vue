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
          <span class="headline">Digite uma senha:</span>
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
        this.Erros.fieldCodMsg = "";
        console.log("REQUISIÇÃO GET PARA O BACKEND VALIDANDO CODIGO");

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
              console.log(response.data);

              axios
                .post(
                  `http://${config.api.host}:${config.api.port}/perfis`,
                  {
                    foto: 'Perfil1.png',
                    nome: 'Usuario',
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
                    console.log(response.data)
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

      //codigo para verificar SMS
      // if (this.codigoAut.length != 6) {
      //   this.Erros.fieldCodMsg = "O Código deve possuir 6 Digitos";
      // } else {
      //   this.Erros.fieldCodMsg = "";
      //   console.log("REQUISIÇÃO GET PARA O BACKEND VALIDANDO CODIGO");

      //   //Caso codigo for validado pelo backend
      //   if (this.codigoAut == "666969") {
      //     let user = {
      //       telefone: this.userTel,
      //       codigo: this.codigoAut
      //     };
      //     localStorage.setItem("USER", JSON.stringify(user));
      //     //Redireciona para a home
      //     this.$router.push("Home");
      //   }
      //   //Caso codigo nao for validado pelo backend
      //   else {
      //     this.Erros.fieldCodMsg = "Código inserido inválido";
      //   }
      // }
    },
    verificaTel() {
      if (this.userTel.length < 18) {
        this.Erros.fieldTelMsg = "O Telefone deve possuir 10 ou 11 Digitos";
      } else {
        this.Erros.fieldTelMsg = "";
        this.dialogCod = true;
        //Apenas para SMS
        //console.log("REQUISIÇÃO POST PARA BACKEND ENVIAR SMS");
      }
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