
<template>
  <v-content>
    <v-row class="c-con-display">
      <v-col cols="12" class="c-margin-auto">
        <v-avatar size="120">
          <img src="../assets/Perfil/perfil5.jpg" />
           <v-btn class="mx-2 c-changePic" fab dark small absolute bottom right color="cyan">
         <v-icon dark>mdi-pencil</v-icon>
        </v-btn>
        </v-avatar>
      </v-col>
      <v-col col="12" class="c-col">
        <v-row>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id='rTotal'>{{usuario.totalRoles}}</span>
          </v-col>
          <v-col cols="col-6" class="c-col1">
            <span class="headline" id='vTotal'>R$ {{usuario.totalPago}}</span>
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
      <v-col cols="1"></v-col>
      <v-col cols="9">
        <v-text-field
          label="Nome"
          :disabled="nameCheck"
          @blur="nameFocusOut"
          v-model=usuario.user
          color="#033"
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%" @click="nameCheck = false; oldName = usuario.user">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="9">
        <v-text-field
          label="Celular"
            v-model=usuario.tel
          class="c-text-field"
          :disabled="numberCheck"
          @blur="numberFocusOut"
          v-mask='mask'
        ></v-text-field>
      </v-col>
      <v-col cols="2">
        <v-btn text icon color="gray" height="100%" @click="numberCheck=false; oldPhone = usuario.tel">
          <v-icon>edit</v-icon>
        </v-btn>
      </v-col>
    </v-row>


  <v-dialog v-model="dialogErro" persistent max-width="350  ">
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

    <v-row class="btn-align">
      <v-col cols="6" class="c-margin-auto">
        <v-btn color="error" dark large :outlined="true" width="75%">Sair</v-btn>
      </v-col>
      <v-col cols="6" class="c-margin-auto">
        <v-btn color="error" dark large width="75%">Apagar Conta</v-btn>
      </v-col>
    </v-row>
  </v-content>

</template>
<script>
import { mask } from "vue-the-mask";
import config from "../assets/dados/config";

export default {
  directives: {
      mask
    },
  data() {
    return {
    config : config,
     oldPhone : "",
     oldName : "",
     dialogErro : false,
     dialogErro2: false,
        usuario: 
        { id: 1, totalRoles: "15", totalPago: "589,88", user: "Ana Banana", tel: "43996150002"}
        ,  
      nameCheck: true,
      numberCheck: true,
      mask: "+55 (##) #####-####"
    };
  },
  methods: {
    nameFocusOut() {
      this.nameCheck = true;
     if( this.usuario.user.length < 3){
        this.dialogErro2 = true;
        this.usuario.user = this.oldName;
      }
    },
    numberFocusOut() {
      this.numberCheck = true;
      if( this.usuario.tel.length < 19){
        this.dialogErro = true;
        this.usuario.tel = this.oldPhone;
      }
    },
  }
};
</script>
<style scoped>
.c-changePic{
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
#vTotal{
   font-family: "Centaur" !important;

   font-weight: 1000;
}
#rTotal{
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
</style>
