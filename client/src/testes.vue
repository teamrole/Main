<template>
  <v-content>
    <div class="c-options">
      <v-menu bottom left>
        <template v-slot:activator="{ on }">
          <v-btn dark icon v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item @click="dialog.FechaParcial = true; selectErro=false">
            <v-list-item-title>Sair do Role</v-list-item-title>
          </v-list-item>
          <v-list-item @click="dialog.FechaSala = true">
            <v-list-item-title>Fechar Sala</v-list-item-title>
          </v-list-item>
          <v-list-item @click="dialog.CodSala = true">
            <v-list-item-title>Código da Sala</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
    <div class="c-participantes">
      <t-avatar
        class="c-participante"
        v-for="pessoa in pessoas"
        :key="pessoa.id"
        :nome="pessoa.nome"
        :avatar="pessoa.foto"
      />
    </div>
    <v-container class="c-list-container" fluid>
      <v-list class="c-list">
        <v-list-item v-for="pedido in items" :key="pedido.id" :dense="true" class="c-list-item">
          <v-avatar tile size="20px">
            <v-img :src="require(`@/assets/Icon/${item.tipo}.png`)"></v-img>
            <v-img :src="require(`@/assets/Icon/bebida.png`)"></v-img>
          </v-avatar>

          <v-list-item-content class="c-list-item-content">
            <v-list-item-title
              class="c-item-lista"
              v-text="pedido.quantidade + 'x ' + (pedido.item.nome ? pedido.item.nome : pedido.item.tipo)"
            ></v-list-item-title>
            <span class="c-item-lista">
              <b>{{pedido.perfil.length}}</b>
              <v-icon class="c-icon-item-lista" color="black">people</v-icon>
            </span>
          </v-list-item-content>

          <v-list-item-icon style="display:inline-block">
            <v-icon class="c-secundary">monetization_on</v-icon>
            <span
              class="c-secundary c-item-preco"
            >{{parseFloat(pedido.item.valor * pedido.quantidade).toFixed(2)}}</span>

            <v-btn class="c-nopadding" :ripple="false" icon small text @click="editarItem(pedido)">
              <v-icon class="c-secundary">edit</v-icon>
            </v-btn>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
    </v-container>
    <v-footer app fixed color="primary" class="c-footer" :padless="true">
      <div class="c-total">
        <span>Total:</span>
        <span class="c-valor">{{totalDoRole.toFixed(2)}} R$</span>
        <br />
        <span>Meu total:</span>
        <span class="c-valor">{{totalPessoal.toFixed(2)}} R$</span>
      </div>
      <div class="flex-grow-1"></div>
      <v-btn text icon x-large class="c-nopadding c-btn-add" @click="novoItem(); selectErro=false">
        <v-icon color="white" class="c-btn-add-icon">add</v-icon>
      </v-btn>
    </v-footer>

    <v-dialog v-model="dialog.Edit" persistent max-width="290">
      <v-card>
        <v-card>
          <v-card-title>
            <span class="headline">{{AcaoItem}} item</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="8">
                  <v-text-field label="Descrição (Opcional)" v-model="itemSendoEditado.nome"></v-text-field>
                </v-col>
                <v-col cols="4">
                  <v-text-field type="number" label="Qtd" v-model="itemSendoEditado.quantidade"></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-select
                    :items="['bebida', 'comida', 'outros']"
                    v-model="itemSendoEditado.tipo"
                    label="Tipo"
                    required
                  ></v-select>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    :error="precoErro"
                    @change="verificaPreco()"
                    label="Preço"
                    v-model="itemSendoEditado.preco"
                    prefix="$"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-select
                    :error="selectErro"
                    :items="this.pessoas"
                    item-text="nome"
                    item-value="id"
                    label="Pagantes"
                    v-model="itemSendoEditado.perfil"
                    small-chips
                    multiple
                    required
                    @blur="selectErro = itemSendoEditado.perfil.length <= 0"
                  >
                    <v-btn slot="prepend-item" color="primary" block @click="toggleSelect">Todos</v-btn>
                  </v-select>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-btn color="blue darken-1" text @click="dialog.Excluir = true; dialog.Edit = false">
              <v-icon color="red">delete</v-icon>
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn color="blue darken-1" text @click="dialog.Edit = false">Cancelar</v-btn>
            <v-btn
              color="blue darken-1"
              text
              @click="salvarItem(itemSendoEditado) ? dialog.Edit = false:null"
            >Salvar</v-btn>
          </v-card-actions>
        </v-card>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.Excluir" persistent max-width="290">
      <v-card>
        <v-card-title
          class="headline"
        >{{itemSendoEditado.nome ? itemSendoEditado.nome : itemSendoEditado.tipo}}</v-card-title>
        <v-card-text>
          Quantidade:{{itemSendoEditado.quantidade}}
          <br />
          Preço:{{itemSendoEditado.preco}}
          <br />
          Pagantes: {{itemSendoEditado.perfil.length}}
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="red darken-1"
            text
            @click.stop="dialog.Excluir = false; excluirItem(itemSendoEditado)"
          >Excluir</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="red darken-1" text @click="dialog.Excluir = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.FechaParcial" persistent max-width="290">
      <v-card>
        <v-card-title>Sair do role</v-card-title>
        <v-card-text>
          Deseja Realmente fechar o role parcial?
          Você não podera retornar ao role.
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="fecharRoleParcial()">Sair</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="red darken-1" text @click="dialog.FechaParcial = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.FechaSala" persistent max-width="290">
      <v-card>
        <v-card-title>Sair do role</v-card-title>
        <v-card-text>
          Deseja Realmente fechar o role?
          Isso Finalizará o role para todos.
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="fecharRole()">Finalizar</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="red darken-1" text @click="dialog.FechaSala = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.CodSala" persistent max-width="290">
      <v-card>
        <v-card-title>Código da sala</v-card-title>
        <v-card-text>
          <h1 style="text-align: center">{{codigoDaSala}}</h1>
        </v-card-text>
        <v-card-actions>
          <div class="flex-grow-1"></div>
          <v-btn color="black" text @click="dialog.CodSala = false">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.FechaItem" persistent max-width="290">
      <v-card>
        <v-card-title>Sua conta é:</v-card-title>
        <v-card-text>
          <h1
            style="text-align: center"
          >{{(parseFloat(totalPessoal).toFixed(2) + "").replace(".", ",")}}R$</h1>
        </v-card-text>
        <v-card-actions>
          <div class="flex-grow-1"></div>
          <v-btn color="black" text @click="$router.push('home')">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template> 


<style scoped>
.c-options {
  display: inline-block;
  position: fixed;
  top: 0;
  right: 0;
  z-index: 30;
  font-size: 2em;
  padding-right: 20px;
}
.c-valor {
  font-weight: bold;
  float: right;
  font-size: 1em;
  padding-left: 10px;
}
.c-total {
  font-size: 0.9em;
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
import axios from "axios";
export default {
  methods: {
    recalculaTotal() {
      //calcula total do role
      let vm = this;
      vm.totalDoRole = 0;
      this.items.map(a => (vm.totalDoRole += a.preco * a.quantidade));
      //calcula parcial do role
      vm.totalPessoal = 0;
      this.items.map(
        a =>
          (vm.totalPessoal +=
            a.perfil.id == vm.idPessoa ? a.preco * a.quantidade : 0)
      );
    },
    novoItem() {
      this.AcaoItem = "Novo";
      this.itemSendoEditado = {
        id: null,
        tipo: "outros",
        nome: null,
        preco: null,
        perfil: []
      };
      this.dialog.Edit = true;
    },
    salvarItem(item) {
      //valida preco
      console.log(item);  
      item.preco = parseFloat(item.preco) || 0;
      this.precoErro = item.preco <= 0;
      console.log(this.precoErro);
      //valida pagantes
      this.selectErro = item.perfil.length <= 0;
      if (!this.precoErro && !this.selectErro) {
        this.precoErro = false;
        this.selectErro = false;
        if (this.AcaoItem == "Novo") {
          console.log("REQUISIÇÃO POST PARA BACKEND");
          //No response Atualizar itens
          this.recalculaTotal();
          let vm = this.itemSendoEditado.nome + '';
          axios
            .post("http://5localhost:6969/pedidos", 
            {
              id: 1,
              pedido: [
                {
                  item: {
                    nome: this.itemSendoEditado.nome + '',
                    valor: this.itemSendoEditado.valor
                  },
                  quantidade: this.itemSendoEditado.quantidade,
                  perfil: [
                    {
                      id: 1,
                      usuario: {
                          id: 1
                        }
                    }
                  ]
                }
              ]
            }
            ,{
              auth: { username: "43999032081", password: "admin" }
            })
            .then(
              response => {
                this.items = response.data;
              },
              error => {
                console.log(error);
              }
            );
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
      this.dialog.Edit = true;
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
    fecharRoleParcial() {
      console.log("REQUISIÇÃO POST PARA BACKEND (FECHAR ROLE)");
      this.dialog.FechaItem = true;
      this.dialog.FechaParcial = false;
      //no response exibir o total
    },
    opcoesSala() {},
    toggleSelect() {
      this.itemSendoEditado.pessoas = this.pessoas.map(a => a.id);
    },
    atualizaJson() {
      //Carrega itens da sala
      axios
        .get(`http://localhost:6969/pedidos/salas/${this.idSala}`, {
          auth: { username: "43999032081", password: "admin" }
        })
        .then(
          response => {
            this.items = response.data;
          },
          error => {
            console.log(error.data);
          }
        );
      console.log("Requisição backend para atualizar os itens");
      this.recalculaTotal();
     
      axios
        .get(`http://localhost:6969/salas/${this.idSala}/usuarios`, {
          auth: { username: "43999032081", password: "admin" }
        })
        .then(
          response => {
            this.pessoas = response.data;
          },
          error => {
            console.log(error);
          }
        );
    }
  },
  components: {
    "t-avatar": avatar
  },
  data() {
    return {
      totalPessoal: 0,
      totalDoRole: 0,
      codigoDaSala: "",
      idSala: 1,
      precoErro: false,
      selectErro: false,
      dialog: {
        Edit: false,
        Excluir: false,
        FechaParcial: false,
        FechaSala: false,
        CodSala: false,
        FechaItem: false
      },
      dialogMsg: "",
      AcaoItem: "Editar",
      itemSendoEditado: {
        id: null,
        tipo: null,
        nome: null,
        preco: null,
        quantidade: null,
        perfil: [1]
      },
      items: [],
      pessoas: [...pessoasJson],
      idPessoa: 1,
      intervaloAtualiza: setInterval(() => {
        this.atualizaJson();
      }, 100000000000)
    };
  },
  mounted() {
    this.atualizaJson();
  }
};
</script>