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

    <v-container class="c-list-container" fluid>
      <v-list class="c-list">
        <v-list-item v-for="item in items" :key="item.id" :dense="true" class="c-list-item">
          <v-avatar tile size="20px">
            <v-img :src="require(`@/assets/Icon/${item.tipo}.png`)"></v-img>
          </v-avatar>

          <v-list-item-content class="c-list-item-content">
            <v-list-item-title
              class="c-item-lista"
              v-text="item.descricao ? item.descricao : item.tipo "
            ></v-list-item-title>
            <span class="c-item-lista">
              <b>{{item.pessoas.length}}</b>
              <v-icon class="c-icon-item-lista" color="black">people</v-icon>
            </span>
          </v-list-item-content>

          <v-list-item-icon>
            <v-icon class="c-secundary">monetization_on</v-icon>
            <span class="c-secundary">&nbsp&nbsp{{item.preco.toFixed(2)}}</span>

            <v-btn x-small text @click="editarItem(item.id)">
              <v-icon class="c-secundary">edit</v-icon>
            </v-btn>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
    </v-container>

    <v-dialog v-model="dialog" persistent max-width="290">
      <v-card>
        <v-card>
          <v-card-title>
            <span class="headline">{{itemSendoEditado.descricao}}</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field label="Descrição (Opcional)" :value="itemSendoEditado.descricao"></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select
                    :items="['bebida', 'comida', 'outros']"
                    v-model="itemSendoEditado.tipo"
                    label="Tipo"
                    required
                  ></v-select>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field label="Preço"></v-text-field>
                  <span>{{itemSendoEditado.pessoas}}</span>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select
                    :items="this.pessoas"
                    item-text="nome"
                    item-value="id"
                    label="Pagantes"
                    v-model="itemSendoEditado.pessoas"
                    chips
                    multiple
                    required
                  ></v-select>
                </v-col>
              </v-row>
            </v-container>
            <small>*indicates required field</small>
          </v-card-text>
          <v-card-actions>
            <div class="flex-grow-1"></div>
            <v-btn color="blue darken-1" text @click="dialog = false">Close</v-btn>
            <v-btn color="blue darken-1" text @click="dialog = false">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-card>
    </v-dialog>
  </v-content>
</template> 


<style scoped>
.c-list-item {
  border-bottom: 1px solid var(--v-primary-lighten5);
}
.c-list-container {
  position: relative;
  overflow: hidden;
}
.c-list-item-content {
  padding-left: 10px;
}
.c-item-lista {
  font-size: 0.9em;
}
.c-icon-item-lista {
  font-size: 1.2em;
}
.c-participantes {
  white-space: nowrap;
  overflow-x: scroll;
  overflow-y: visible;
  background-color: white;
  box-shadow: 0 4px 10px -3px gray;
}
.c-participante {
  margin: 6px;
}
.c-secundary {
  color: var(--v-accent-darken2) !important;
}
</style>

<script>
import avatar from "./Templates/avatar-lobby";

export default {
  methods: {
    editarItem: function(idItem) {
      this.dialog = true;
      const vm = this;
      this.itemSendoEditado = this.items.find(x => x.id == idItem)
      console.log(this.itemSendoEditado)
      setTimeout(() => {
        vm.dialog = false;
      }, 20000);
    }
  },
  components: {
    "t-avatar": avatar
  },
  data() {
    return {
      dialog: false,
      itemSendoEditado: {
        id: 1,
        tipo: "bebida",
        descricao: "Coca-Cola",
        preco: 20.1,
        pessoas: [1, 2, 3]
      },
      items: [
        {
          id: 1,
          tipo: "bebida",
          descricao: "Coca-Cola",
          preco: 20.1,
          pessoas: [1, 2, 3]
        },
        {
          id: 2,
          tipo: "outros",
          descricao: "",
          preco: 20.1,
          pessoas: [1, 2, 3]
        },
        {
          id: 3,
          tipo: "comida",
          descricao: "Arroz",
          preco: 20.1,
          pessoas: [1, 2, 3]
        },
        {
          id: 4,
          tipo: "outros",
          descricao: "Pasta de dentes",
          preco: 20.1,
          pessoas: [1, 2, 3]
        },
        {
          id: 5,
          tipo: "outros",
          descricao: "Pasta de dentes",
          preco: 20.1,
          pessoas: [1, 2, 3]
        },
        {
          id: 6,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 7,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 8,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 9,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 10,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 11,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 12,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        },
        {
          id: 13,
          tipo: "comida",
          descricao: "Macarronada",
          preco: 20,
          pessoas: [1, 2, 3]
        }
      ],
      pessoas: [
        { id: 1, nome: "izabelly", avatar: "Perfil1.jpg" },
        { id: 2, nome: "Amanda", avatar: "Perfil2.jpg" },
        { id: 3, nome: "ana", avatar: "Perfil3.jpg" },
        { id: 4, nome: "Yuri", avatar: "Perfil4.jpg" },
        { id: 5, nome: "Yuri", avatar: "Perfil4.jpg" },
        { id: 6, nome: "Yuri", avatar: "Perfil4.jpg" },
        { id: 7, nome: "Yuri", avatar: "Perfil4.jpg" },
        { id: 8, nome: "Yuri", avatar: "Perfil4.jpg" }
      ]
    };
  }
};
</script>