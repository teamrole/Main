<template>
  <v-content>
    <div class="c-participantes">
      <t-avatar
        class="c-participante"
        v-for="pessoa in pessoas"
        :key="pessoa.id"
        :nome="pessoa.nome"
        :avatar="pessoa.avatar"
      />
    </div>
    <v-container>
      <div class="c-lista-item">
        <v-list-item
          v-for="item in items"
          :key="item.id"
          @click.stop
          :dense="true"
        >
          <v-avatar tile size="20px"> 
            <v-img :src="require(`@/assets/Icon/${item.tipo}.png`)"></v-img>
          </v-avatar>

          <v-list-item-content>
            <v-list-item-title v-text="item.descricao"></v-list-item-title>
            <v-list-item-title v-text="item.pessoas.length"></v-list-item-title>
          </v-list-item-content>

          <v-list-item-icon>
            <span>{{item.preco}} R$</span>
          </v-list-item-icon>
        </v-list-item>
      </div>
    </v-container>
  </v-content>
</template> 
<style scoped>
.c-icon-item-lista{
  padding: 4px;
}
.c-participantes {
  white-space: nowrap;
  overflow-x: scroll;
  overflow-y: visible;
}
.c-participante {
  margin: 6px;
}
.c-lista-item {
  height: 10%;
  overflow-y: scroll;
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
      items:[
        {id: 1,tipo: "bebida", descricao:"Coca-Cola", preco: 20.1, pessoas:[1,2,3]},
        {id: 2,tipo: "outros", descricao:"Cocaina", preco: 20.1, pessoas:[1,2,3]},
        {id: 3,tipo: "comida", descricao:"Arroz", preco: 20.1, pessoas:[1,2,3]},
        {id: 4,tipo: "outros", descricao:"Pasta de dentes", preco: 20.1, pessoas:[1,2,3]},
        {id: 5,tipo: "comida", descricao:"Macarronada", preco: 20, pessoas:[1,2,3]}
      ],
      pessoas: [
        {id: 1, nome: "izabelly", avatar: "Perfil1.jpg" },
        {id: 2, nome: "Amanda",avatar: "Perfil2.jpg"},
        {id: 3, nome: "ana",avatar: "Perfil3.jpg"},
        {id: 4, nome: "Yuri",avatar: "Perfil4.jpg"},
        {id: 5, nome: "Yuri",avatar: "Perfil4.jpg"},
        {id: 6, nome: "Yuri",avatar: "Perfil4.jpg"},
        {id: 7, nome: "Yuri",avatar: "Perfil4.jpg"},
        {id: 8, nome: "Yuri",avatar: "Perfil4.jpg"}
      ]
    };
  }
};
</script>