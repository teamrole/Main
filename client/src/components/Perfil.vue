
<template>
  <v-content class="c-content">
    <v-row class="c-con-display">
      <v-col cols="12" class="c-margin-auto">
        <v-avatar size="120">
          <img :src="(perfil.foto?perfil.foto:fotoDefault)" />

          <div class="c-label c-changePic">
            <upload-btn title @file-update="onFileChange" class="c-label">
              <template slot="icon">
                <v-icon>edit</v-icon>
              </template>
            </upload-btn>
          </div>
        </v-avatar>
      </v-col>
      <v-col col="12" class="c-col">
        <v-row>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id="rTotal">{{totalRoles}}</span>
          </v-col>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id="vTotal">R$ {{(totalPago).toFixed(2)}}</span>
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

    <v-row justify="center" class="c-row-marg" style="padding-left:25px">
      <v-col cols="10">
        <v-text-field
          label="Nome"
          :disabled="nameCheck"
          @blur="nameFocusOut"
          v-model="perfil.nome"
          color="#033"
          :error="ErroNome"
          ref="inputNome"
          id="inputNome"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%" @click="nameCheck = false; applyFocus()">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center" class="c-row-marg">
      <v-col cols="11">
        <v-text-field
          label="Celular"
          v-model="usuarioLogado.celular"
          class="c-text-field"
          :disabled="true"
          v-mask="mask"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row v-if="primeiroLogin">
      <v-col cols="12">
        <div style="width:10%;display:inline-block"></div>
        <v-btn color="green" width="80%" @click="validaNome()?goHome():null;">Continuar</v-btn>
      </v-col>
    </v-row>

    <v-row v-else class="c-row-marg" style="text-align: center">
      <v-col>
        <v-btn
          class="c-btn-pass"
          color="primary"
          dark
          :outlined="true"
          width="90%"
          @click="dialogSenha = true"
        >Mudar Senha</v-btn>
      </v-col>
    </v-row>
    <v-row v-if="!primeiroLogin" class="c-row-marg">
      <v-col cols="6">
        <div style="width:10%;display:inline-block"></div>
        <v-btn
          color="error"
          dark
          :outlined="true"
          width="90%"
          @click="dialogConfirmaMsg='Deseja mesmo fazer o logoff?';dialogConfirma=true"
        >Sair</v-btn>
      </v-col>
      <v-col cols="6">
        <v-btn
          color="error"
          dark
          width="90%"
          @click="dialogConfirmaMsg='exclui';dialogConfirma=true"
        >Desativar Conta</v-btn>
      </v-col>
    </v-row>

    <v-dialog v-model="dialogConfirma" persistent max-width="290">
      <v-card>
        <v-card-title></v-card-title>
        <v-card-text>
          <h3>{{dialogConfirmaMsg === "exclui"?"Deseja mesmo desativar sua conta?":"Deseja fazer o logoff?"}}</h3>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="dialogConfirma=false">Cancelar</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn
            color="green darken-3"
            text
            @click="confirmaDialog(dialogConfirmaMsg)"
          >{{dialogConfirmaMsg === "exclui"?"Desativar":"Logoff"}}</v-btn>
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

    <v-dialog v-model="dialogSenha" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">Digite a nova senha</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field type="password" label="Senha" v-model="senha"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="dialogSenha = false; senha = ''">Cancelar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="dialogSenha = false; senha = ''">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template>

<script>
import { mask } from "vue-the-mask";
import config from "../assets/dados/config";
import firebase from "firebase/app";
import "firebase/storage";
import axios from "axios";
import UploadButton from "vuetify-upload-button";

export default {
  components: {
    "upload-btn": UploadButton
  },
  directives: {
    mask
  },
  data() {
    return {
      config: config,
      primeiroLogin: localStorage.getItem("firstLogin"),
      oldName: "",
      dialogConfirmaMsg: "",
      dialogConfirma: false,
      ErroNome: false,
      dialogLoading: false,
      totalPago: 0,
      totalRoles: 0,
      fileFoto: null,
      fotoChanged: false,
      storageRef: null,
      senha: "",
      dialogSenha: false,
      fotoDefault:
        "https://firebasestorage.googleapis.com/v0/b/i-role.appspot.com/o/src%2Ffoto-padrao.png?alt=media&token=7899e09b-3157-4c49-a18c-66ab3f20d067",
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
    validaNome(e) {
      if (
        this.perfil.nome ? (this.perfil.nome.length >= 3 ? true : false) : false
      ) {
        return true;
      } else {
        this.nameCheck = false;
        setTimeout(() => this.$refs.inputNome.focus(), 500);
      }
    },
    goHome() {
      localStorage.removeItem("firstLogin");
      this.$router.push("Home");
    },
    nameFocusOut() {
      if (
        this.perfil.nome ? (this.perfil.nome.length >= 3 ? false : true) : true
      ) {
        this.ErroNome = true;
        this.$refs.inputNome.focus();
      } else {
        this.nameCheck = true;
        this.salvaLogin();
      }
    },
    applyFocus() {
      setTimeout(() => {
        this.$refs.inputNome.focus();
      }, 500);
    },
    onFileChange(file) {
      this.fileFoto = file;
      this.fotoChanged = true;

      this.perfil.foto = URL.createObjectURL(this.fileFoto);

      if (file) {
        const name = this.usuarioLogado.id + ".jpg";
        const metadata = { contentType: file.type };
        const task = this.storageRef.child(name).put(file, metadata);

        this.dialogLoading = true;
        task
          .then(snapshot => snapshot.ref.getDownloadURL())
          .then(url => {
            this.perfil.foto = url;
            this.dialogLoading = false;
            this.salvaLogin();
          })

          .catch(e => {
            this.dialogLoading = false;
          });
      }
    },
    salvaLogin() {
      this.dialogLoading = true;
      axios
        .put(
          `${config.api.url}/perfis/${this.usuarioLogado.perfil.id}`,
          this.perfil,
          {
            auth: config.api.auth
          }
        )
        .then(
          response => {
            this.dialogLoading = false;
            this.perfil = response.data;
          },
          error => {
            this.dialogLoading = false;
            console.log(error);
          }
        );
    },
    atualizaUsuario() {
      let vm = this;
      this.dialogLoading = true;
      axios
        .get(`${config.api.url}/usuarios/${this.usuarioLogado.id}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            this.dialogLoading = false;
            this.perfil = response.data.perfil;
          },
          error => {
            this.dialogLoading = false;
            console.log(error);
          }
        );

      this.dialogLoading = true;
      axios
        .get(
          `${config.api.url}/historicos/usuarios/${this.usuarioLogado.perfil.id}`,
          {
            auth: config.api.auth
          }
        )
        .then(
          response => {
            this.dialogLoading = false;
            vm.totalPago = 0;
            if (response.data) {
              vm.totalRoles = response.data.length;

              response.data.map(historico => {
                vm.totalPago += historico.totalParcial;
              });
            }
          },
          error => {
            this.dialogLoading = false;
            console.log(error);
          }
        );
    },
    desativaUsuario() {
      this.dialogLoading = true;
      axios
        .delete(`${config.api.url}/usuarios/${this.usuarioLogado.id}`, {
          auth: config.api.auth
        })
        .then(
          response => {
            this.dialogLoading = false;
            localStorage.clear();
            this.$router.push("/");
          },
          error => {
            this.dialogLoading = false;
            console.log(error);
          }
        );
    },
    confirmaDialog(dialogConfirmaMsg) {
      if (dialogConfirmaMsg === "exclui") {
        this.desativaUsuario();
      } else {
        localStorage.clear();
        this.$router.push("/");
      }
    }
  },
  mounted() {
    console.log(this.primeiroLogin);
    this.atualizaUsuario();

    if (!firebase.apps.length)
      firebase.initializeApp({
        apiKey: "AIzaSyCviymQ96LuL_1XpmiURwOoXX7igF7Yelk",
        authDomain: "i-role.firebaseapp.com",
        databaseURL: "https://i-role.firebaseio.com",
        projectId: "i-role",
        storageBucket: "i-role.appspot.com",
        messagingSenderId: "332147994294",
        appId: "1:332147994294:web:52791194f69163ff390017"
      });
    this.storageRef = firebase.storage().ref();
  }
};
</script>

<style scoped>
.c-changePic {
  position: relative;
  position: absolute;
}
.c-text-field {
  padding-top: 0;
}
.btn-align {
  text-align: right;
}
.c-con-display {
  background: var(--v-primary-lighten3);
  padding: 0 !important;
  margin: 0px;
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
  position: absolute;
  width: 70px;
  height: 35px;
  padding-top: 0;
  cursor: pointer;
  left: 35px;
  top: 45px;
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

.c-row-marg {
  margin-right: 0px !important;
}

.c-btn-pass {
  justify-content: center;
}
</style>