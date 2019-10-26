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
          <span class="headline">Insira o codigo recebido por SMS</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  label="Código"
                  v-model="codigoAut"
                  :error-messages="Erros.fieldCodMsg"
                  class="c-login-cod"
                  @keyup="verificaCodKeyUp()"
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
      if (this.codigoAut.length != 6) {
        this.Erros.fieldCodMsg = "O Código deve possuir 6 Digitos";
      } else {
        this.Erros.fieldCodMsg = "";
        console.log("REQUISIÇÃO GET PARA O BACKEND VALIDANDO CODIGO");

        //Caso codigo for validado pelo backend
        if (this.codigoAut == "666969") {
          let user = {
            telefone: this.userTel,
            codigo: this.codigoAut
          };
          localStorage.setItem("USER", JSON.stringify(user));
          //Redireciona para a home
          this.$router.push("Home");
        }
        //Caso codigo nao for validado pelo backend
        else {
          this.Erros.fieldCodMsg = "Código inserido inválido";
        }
      }
    },
    verificaTel() {
      if (this.userTel.length < 18) {
        this.Erros.fieldTelMsg = "O Telefone deve possuir 10 ou 11 Digitos";
      } else {
        this.Erros.fieldTelMsg = "";
        this.dialogCod = true;
        console.log("REQUISIÇÃO POST PARA BACKEND ENVIAR SMS");
      }
    }
  },
  directives: {
    mask
  },
  data: () => ({
    config : config,
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