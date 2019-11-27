
<template>
  <v-content>
    <v-row class="c-con-display">
      <v-col cols="12" class="c-margin-auto">
        <v-avatar size="120">
          <img :src="(perfil.foto?perfil.foto:fotoDefault)" />
          <v-btn class="mx-2 c-changePic" fab dark small absolute bottom right color="cyan">
            <div class="c-label">
              <v-file-input @change="onFileChange" class="c-input" accept=".png, .jpg, .jpeg" />
              <v-icon dark id="c-img-selector">mdi-pencil</v-icon>
            </div>
          </v-btn>
        </v-avatar>
      </v-col>
      <v-col col="12" class="c-col">
        <v-row>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id="rTotal">{{totalRoles}}</span>
          </v-col>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id="vTotal">R$ {{totalPago}}</span>
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
      <v-col cols="9">
        <v-text-field
          label="Nome"
          :disabled="nameCheck"
          @blur="nameFocusOut"
          v-model="perfil.nome"
          color="#033"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn
          text
          icon
          color="gray"
          height="100%"
          @click="nameCheck = false; oldName = perfil.nome"
        >
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="9">
        <v-text-field
          label="Celular"
          v-model="usuarioLogado.celular"
          class="c-text-field"
          :disabled="numberCheck"
          @blur="numberFocusOut"
          v-mask="mask"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn
          text
          icon
          color="gray"
          height="100%"
          @click="numberCheck=false; oldPhone = usuario.celular"
        >
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-dialog v-model="dialogErro" persistent max-width="350">
      <v-card>
        <v-card-title style="color: red">Número Inválido</v-card-title>
        <v-card-text>
          <h3 style=" color:black">Insira um número de telefone válido!</h3>
        </v-card-text>
        <v-card-actions>
          <div class="flex-grow-1"></div>
          <v-btn color="black" text @click="dialogErro = false">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialogErro2" persistent max-width="350  ">
      <v-card>
        <v-card-title style="color: red">Nome Inválido</v-card-title>
        <v-card-text>
          <h3 style=" color:black">Insira o nome que será apresentado nos rolês</h3>
        </v-card-text>
        <v-card-actions>
          <div class="flex-grow-1"></div>
          <v-btn color="black" text @click="dialogErro2 = false">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-row>
      <v-col cols="6">
        <div style="width:10%;display:inline-block"></div>
        <v-btn color="error" dark :outlined="true" width="90%">Sair</v-btn>
      </v-col>
      <v-col cols="6">
        <v-btn color="error" dark width="90%">Apagar Conta</v-btn>
      </v-col>
    </v-row>
  </v-content>
</template>

<script>
import { mask } from "vue-the-mask";
import config from "../assets/dados/config";

import axios from "axios";

export default {
  directives: {
    mask
  },
  data() {
    return {
      config: config,
      oldPhone: "",
      oldName: "",
      dialogErro: false,
      dialogErro2: false,
      totalPago: 0,
      totalRoles: 0,
      fileFoto: null,
      fotoChanged: false,
      fotoDefault:
        "https://firebasestorage.googleapis.com/v0/b/i-role.appspot.com/o/foto-padrao.png?alt=media&token=46cbd9b0-a561-4f03-9bf7-f47ff106bc0a",
      usuarioLogado: JSON.parse(localStorage.getItem("User")),
      perfil: {
        foto: null,
        id: 0,
        nome: null
      },
      nameCheck: true,
      numberCheck: true,
      mask: "+55 (##) #####-####"
    };
  },
  methods: {
    nameFocusOut() {
      this.nameCheck = true;
      if (this.usuario.perfil.nome.length < 3) {
        this.dialogErro2 = true;
        this.usuario.perfil.nome = this.oldName;
      }
    },
    numberFocusOut() {
      this.numberCheck = true;
      if (this.usuario.celular.length < 19) {
        this.dialogErro = true;
        this.usuario.celular = this.oldPhone;
      }
    },
    onFileChange(file) {
      this.fileFoto = file;
      this.perfil.foto = URL.createObjectURL(this.fileFoto);
      this.fotoChanged = true;
    },
    atualizaUsuario() {
      let vm = this;
      axios
        .get(
          `http://${config.api.host}${config.api.port}/usuarios/${this.usuarioLogado.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            console.log(response.data);
            localStorage.setItem("USER", JSON.stringify(response.data));
          },
          error => {
            console.log(error);
          }
        );

      axios
        .get(
          `http://${config.api.host}:${config.api.port}/historicos/usuarios/${this.usuarioLogado.id}`,
          {
            auth: config.api.auth
          }
        )
        .then(
          response => {
            console.log(response);
            console.log(response.data);
            let totalUsuario = 0;
            if (response.data) {
              response.data.map(historico => {
                totalUsuario += parseInt(
                  historico.sala.pedido
                    .map(ped =>
                      ped.perfil.filter(perf => {
                        return perf.id == vm.usuario.id;
                      }).length > 0
                        ? (ped.item.valor * ped.quantidade) / ped.perfil.length
                        : 0
                    )
                    .reduce((total, valor) => total + valor)
                );
              });
            }
            vm.totalPago = totalUsuario.toFixed(2);
            vm.totalRoles = response.data.length;
          },
          error => {
            console.log(error);
          }
        );
      console.log("Requisição backend para atualizar os itens");
    }
  },
  mounted() {
    this.atualizaUsuario();
  }
};
</script>

<style scoped>
.c-changePic {
  right: -9px;
  top: 86px;
}
.c-text-field {
  padding-top: 0;
}
.btn-align {
  text-align: right;
}
.c-con-display {
  background: var(--v-primary-lighten3);
}
.c-margin-auto {
  text-align: center;
  padding-bottom: 0;
}
#vTotal {
  font-family: "Centaur" !important;

  font-weight: 1000;
}
#rTotal {
  font-family: "Centaur" !important;
  font-weight: 1000;
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
.c-label {
  position: relative;
  width: 70px;
  height: 35px;
  top: 5px;
  padding-top: 0;
  cursor: pointer;
}
.c-input {
  position: absolute;
  z-index: 1;
  top: -19px;
  right: 6px;
  opacity: 0;
  width: 35px;
  height: 35px;
}
</style>