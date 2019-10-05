<template>
  <v-content>
    <v-container fill-height="100" .offset-lg-4>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="4" class="text-centered">
          <v-text-field
            v-mask="'+55 (##) #####-####'"
            label="Insira seu número"
            color="primary"
            :outlined="true"
            :clearable="true"
            :hide-details="true"
            placeholder="+55(99)9 9999-9999"
            v-model="User"
            :error="erroTelefone"
          ></v-text-field>
          <v-btn
            class="btn-marger"
            color="primary"
            href="#"
            elevation="3"
            width="100%"
            v-bind:large="true"
            @click="autentica()"
          >Enviar</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-content>
</template>

<style scoped>
.text-centered {
  text-align: right;
}
.btn-marger {
  color: white;
  margin-top: 15px;
}
</style>
<script>
import { mask } from "vue-the-mask";

export default {
  methods: {
    autentica() {
      let auxUser = "";
      if (this.User) {
        auxUser = this.User.replace(/[ ()\-\+]/g, "");
        if (auxUser.length < 12 || auxUser.length > 13) {
          this.erroTelefone = true;
          return;
        }
      } else {
        this.erroTelefone = true;
        return;
      }
      this.erroTelefone = false;
      console.log("REQUISICAO POST PARA BACK COM TELEFONE: " + auxUser + 
      "\nNO CASO DE SUCESSO, GUARDA TELEFONE E 'SENHA' NO LOCAL STORAGE")
      localStorage.User = auxUser;
      this.$router.push('Perfil')
    }
  },
  directives: {
    mask
  },
  data() {
    return {
      erroTelefone: false,
      User: ""
    };
  }
};
</script>