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
              v-text="item.descricao ? item.descricao : item.tipo"
            ></v-list-item-title>
            <span class="c-item-lista">
              <b>{{item.pessoas.length}}</b>
              <v-icon class="c-icon-item-lista" color="black">people</v-icon>
            </span>
          </v-list-item-content>

          <v-list-item-icon style="display:inline-block">
            <v-icon class="c-secundary">monetization_on</v-icon>
            <span class="c-secundary c-item-preco">{{parseFloat(item.preco).toFixed(2)}}</span>

            <v-btn class="c-nopadding" :ripple="false" icon small text @click="editarItem(item)">
              <v-icon class="c-secundary">edit</v-icon>
            </v-btn>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
    </v-container>

    <v-footer app fixed color="primary" class="c-footer" :padless="true">
      <div class="c-total">
        <span>Total: </span>
        <span class="c-valor">{{totalDoRole.toFixed(2)}} R$</span>
        <br />
        <span>Meu total: </span>
        <span class="c-valor">{{totalPessoal.toFixed(2)}} R$</span>
      </div>
      <v-btn text icon x-large class="c-nopadding c-btn-add" @click="novoItem(); selectErro=false">
        <v-icon color="white" class="c-btn-add-icon">add</v-icon>
      </v-btn>
      <div class="flex-grow-1"></div>
      <v-btn text icon x-large class="c-nopadding c-btn-add" @click="novoItem(); selectErro=false">
        <v-icon color="white" class="c-btn-add-icon">add</v-icon>
      </v-btn>
    </v-footer>

    <v-dialog v-model="dialogEdit" persistent max-width="290">
      <v-card>
        <v-card>
          <v-card-title>
            <span class="headline">{{AcaoItem}} item</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field label="Descrição (Opcional)" v-model="itemSendoEditado.descricao"></v-text-field>
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
                  <v-text-field
                    :error="precoErro"
                    @change="verificaPreco()"
                    label="Preço"
                    v-model="itemSendoEditado.preco"
                    prefix="$"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-select
                    :error="selectErro"
                    :items="this.pessoas"
                    item-text="nome"
                    item-value="id"
                    label="Pagantes"
                    v-model="itemSendoEditado.pessoas"
                    small-chips
                    multiple
                    required
                    @blur="selectErro = itemSendoEditado.pessoas.length <= 0"
                  >
                    <v-btn slot="prepend-item" color="primary" block @click="toggleSelect">Todos</v-btn>
                  </v-select>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-btn color="blue darken-1" text @click="dialogExcluir = true; dialogEdit = false">
              <v-icon color="red">delete</v-icon>
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn color="blue darken-1" text @click="dialogEdit = false">Cancelar</v-btn>
            <v-btn
              color="blue darken-1"
              text
              @click="salvarItem(itemSendoEditado) ? dialogEdit = false:null"
            >Salvar</v-btn>
          </v-card-actions>
        </v-card>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogExcluir" persistent max-width="290">
      <v-card>
        <v-card-title
          class="headline"
        >{{itemSendoEditado.descricao ? itemSendoEditado.descricao : itemSendoEditado.tipo}}</v-card-title>
        <v-card-text>
          Preço:{{itemSendoEditado.preco}}
          <br />
          Pagantes: {{itemSendoEditado.pessoas.length}}
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="red darken-1"
            text
            @click.stop="dialogExcluir = false; excluirItem(itemSendoEditado)"
          >Excluir</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="red darken-1" text @click="dialogExcluir = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template> 


<style scoped>
.c-valor{
  font-weight: bold;
  float: right;
  font-size: 1em;
  padding-left: 10px; 
}
.c-total{
  font-size: .9em
}
.c-invalid {
  background-color: rebeccapurple;
}
.c-footer {
  height: 50px;
}
.c-nopadding {
  padding: 0px !important;
}
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
.c-item-preco {
  padding-left: 6px;
  vertical-align: middle;
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
import pessoasJson from "../assets/dados/Lobby-pessoas";
import itemsJson from "../assets/dados/Lobby-items";
export default {
  methods: {
    recalculaTotal() {

      //calcula total do role
      let vm = this;
      vm.totalDoRole = 0;
      this.items.map(a => (vm.totalDoRole += a.preco));

      //calcula parcial do role
      vm.totalPessoal = 0;
      this.items.map(a => (vm.totalPessoal += a.pessoas.includes(vm.idPessoa)?a.preco:0    ));
    },
    novoItem() {
      this.AcaoItem = "Novo";
      this.itemSendoEditado = {
        id: null,
        tipo: "outros",
        descricao: null,
        preco: null,
        pessoas: []
      };
      this.dialogEdit = true;
    },
    salvarItem(item) {
      //valida preco
      item.preco = parseFloat(item.preco) || 0;
      this.precoErro = item.preco <= 0;
      console.log(this.precoErro);
      //valida pagantes
      this.selectErro = item.pessoas.length <= 0;

      if (!this.precoErro && !this.selectErro) {
        this.precoErro = false;
        if (this.AcaoItem == "Novo") {
          this.items.push(item);
          console.log("REQUISIÇÃO POST PARA BACKEND");
          //No response Atualizar itens
          this.recalculaTotal();
          return true;
        } else if (this.AcaoItem == "Editar") {
          let alteraIndex = this.items.findIndex(x => x.id === item.id);
          this.items[alteraIndex] = item;
          console.log("REQUISIÇÃO EDIT PARA BACKEND");
          //no response atualizar itens
          this.recalculaTotal();
          return true;
        }
      } else {
        return false;
      }
    },
    excluirItem(item) {
      let alteraIndex = this.items.findIndex(x => x.id === item.id);
      this.items.splice(alteraIndex, 1);
      console.log("REQUISIÇÃO DELETE PARA BACKEND");
      //no response atualizar itens
      this.recalculaTotal();
    },
    editarItem(item) {
      this.AcaoItem = "Editar";
      this.itemSendoEditado = { ...item };
      this.dialogEdit = true;
    },
    verificaPreco() {
      if (this.itemSendoEditado.preco.length <= 0) this.precoErro = true;
      else {
        let preco = (this.itemSendoEditado.preco + "").replace(/\,/g, ".");
        preco = preco.includes(".") ? preco : preco + ".00";
        preco = preco.replace(/[^0-9.]/g, "");
        preco = preco.split(".");
        preco = preco.slice(0, -1).join("") + "." + preco.slice(-1);
        this.itemSendoEditado.preco = parseFloat(preco).toFixed(2);
        this.precoErro = false;
      }
    },
    toggleSelect() {
      this.itemSendoEditado.pessoas = this.pessoas.map(a => a.id);
    }
  },
  components: {
    "t-avatar": avatar
  },
  data() {
    return {
      totalPessoal: 0,
      totalDoRole: 0,
      precoErro: false,
      selectErro: false,
      dialogEdit: false,
      dialogExcluir: false,
      dialogMsg: "",
      AcaoItem: "Editar",
      itemSendoEditado: {
        id: null,
        tipo: null,
        descricao: null,
        preco: null,
        pessoas: []
      },
      items: [...itemsJson],
      pessoas: [...pessoasJson],
      idPessoa : 1
    };
  },
  mounted() {
    this.recalculaTotal();
  }
};
</script>