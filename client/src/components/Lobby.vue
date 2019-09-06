<template>
  <v-content>
    <v-container>
      <div class="c-participantes">
        <t-avatar class="c-participante" v-for="item in items" :key="item.nome"/>
      </div>
    </v-container>
  </v-content>
</template> 
<style scoped>
.c-participantes {
  white-space: nowrap;
  overflow-x: scroll;
  overflow-y: visible;
}
.c-participante{
  margin: 6px;
}
</style>

<script>
import avatar from "./Templates/avatar-lobby";

export default {
  components: {
    "t-avatar": avatar
  },
  data() {
    return {
      items: [
        {
          nome: "pessoa1",
          avatar: "../assets/Perfil/Perfil1.jpg"
        },{
          nome: "pessoa2",
          avatar: "../assets/Perfil/Perfil1.jpg"
        },{
          nome: "pessoa3",
          avatar: "../assets/Perfil/Perfil1.jpg"
        },{
          nome: "pessoa4",
          avatar: "../assets/Perfil/Perfil1.jpg"
        },{
          nome: "pessoa5",
          avatar: "../assets/Perfil/Perfil1.jpg"
        },{
          nome: "pessoa6",
          avatar: "../assets/Perfil/Perfil1.jpg"
        }
      ]
    };
  }
};
</script>