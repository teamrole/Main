
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
          :error="ErroNome"
          ref="inputNome"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%" @click="nameCheck = false;">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center">
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

    <v-row>
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
  </v-content>
</template>

<script>
import { mask } from "vue-the-mask";
import config from "../assets/dados/config";
import firebase from "firebase/app";
import "firebase/storage";
import axios from "axios";

export default {
  directives: {
    mask
  },
  data() {
    return {
      config: config,
      oldName: "",
      dialogConfirmaMsg: "",
      dialogConfirma: false,
      ErroNome: false,
      totalPago: 0,
      totalRoles: 0,
      fileFoto: null,
      fotoChanged: false,
      storageRef: null,
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
    nameFocusOut() {
      if (this.perfil.nome.length < 3) {
        this.ErroNome = true;
        this.$refs.inputNome.focus();
      } else {
        this.nameCheck = true;
        this.salvaLogin();
      }
    },
    onFileChange(file) {
      this.fileFoto = file;
      this.fotoChanged = true;

      this.perfil.foto = URL.createObjectURL(this.fileFoto);

      if (file) {
        const name = this.usuarioLogado.id + ".jpg";
        const metadata = { contentType: file.type };
        const task = this.storageRef.child(name).put(file, metadata);

        task
          .then(snapshot => snapshot.ref.getDownloadURL())
          .then(url => {
            this.perfil.foto = url;
            this.salvaLogin();
          })
          .catch(console.error);
      }
    },
    salvaLogin() {
      axios
        .put(
          `http://${config.api.host}${config.api.port}/perfis/${this.usuarioLogado.id}`,
          this.perfil,
          { auth: config.api.auth }
        )
        .then(
          response => {
            this.perfil = response.data;
          },
          error => {
            console.log(error);
          }
        );
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
            this.perfil = response.data.perfil;
          },
          error => {
            console.log(error);
          }
        );

      axios
        .get(
          `http://${config.api.host}${config.api.port}/historicos/usuarios/${this.usuarioLogado.id}`,
          {
            auth: config.api.auth
          }
        )
        .then(
          response => {
            vm.totalPago = 0;
            if (response.data) {
              vm.totalRoles = response.data.length;

              response.data.map(historico => {
                vm.totalPago += historico.totalParcial;
              });
            }
          },
          error => {
            console.log(error);
          }
        );
    },
    desativaUsuario() {
      axios
        .delete(
          `http://${config.api.host}${config.api.port}/usuarios/${this.usuarioLogado.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            localStorage.clear();
            this.$router.push("/");
          },
          error => {
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