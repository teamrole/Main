<template>
  <v-content>
    <div class="c-name">
      <p>{{sala.nome?sala.nome:"Role S/Nome"}}</p>
    </div>

    <div class="c-options">
      <v-menu bottom right>
        <template v-slot:activator="{ on }">
          <v-btn dark icon v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item>
            <v-list-item-title @click.prevent="dialog.FechaParcial = true">Sair do Role</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title @click.prevent="dialog.FechaSala = true">Fechar Sala</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title @click.prevent="dialog.CodSala = true">Código da Sala</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title
              @click.prevent="salaEdtNome = sala.nome; dialog.EdtNomeSala = true"
            >Editar nome da sala</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>

    <div class="c-participantes">
      <t-avatar
        class="c-participante"
        v-for="pessoa in pessoas"
        :key="pessoa.perfil.id"
        :nome="pessoa.perfil.nome?pessoa.perfil.nome:'S/Nome'"
        :avatar="pessoa.perfil.foto?pessoa.perfil.foto:fotoDefault"
      />
    </div>
    <v-container class="c-list-container" fluid>
      <v-list class="c-list">
        <v-list-item v-for="pedido in items" :key="pedido.id" :dense="true" class="c-list-item">
          <v-avatar tile size="20px">
            <v-img :src="require(`@/assets/Icon/${pedido.item.itemTipo}.png`)"></v-img>
          </v-avatar>

          <v-list-item-content class="c-list-item-content">
            <v-list-item-title
              class="c-item-lista"
              v-text="pedido.quantidade + 'x ' + (pedido.item.descricao ? pedido.item.descricao : pedido.item.itemTipo)"
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
                  <v-text-field
                    label="Descrição (Opcional)"
                    v-model="itemSendoEditado.item.descricao"
                  ></v-text-field>
                </v-col>
                <v-col cols="4">
                  <v-text-field
                    type="number"
                    label="Qtd"
                    :error="qtdErro"
                    v-model="itemSendoEditado.quantidade"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-select
                    :items="['BEBIDA', 'COMIDA', 'OUTRO']"
                    v-model="itemSendoEditado.item.itemTipo"
                    label="Tipo"
                    required
                  ></v-select>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    :error="precoErro"
                    @change="verificaPreco()"
                    label="Preço"
                    v-model="itemSendoEditado.item.valor"
                    prefix="$"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-select
                    :error="selectErro"
                    :items="this.pessoasPerfil"
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
        >{{itemSendoEditado.descricao ? itemSendoEditado.descricao : itemSendoEditado.itemTipo}}</v-card-title>
        <v-card-text>
          Quantidade:{{itemSendoEditado.quantidade}}
          <br />
          Preço:{{itemSendoEditado.item.valor}}
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
        <v-card-title>fechar o role</v-card-title>
        <v-card-text>
          Deseja Realmente fechar o role?
          Isso Finalizará o role para todos.
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click.prevent="fecharRole()">Finalizar</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn color="red darken-1" text @click="dialog.FechaSala = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog.CodSala" persistent max-width="290">
      <v-card>
        <v-card-title>Código da sala</v-card-title>
        <v-card-text>
          <h1 style="text-align: center">{{sala.codigo}}</h1>
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

    <v-dialog v-model="dialog.EdtNomeSala" persistent max-width="290">
      <v-card>
        <v-card-title>
          <span class="headline">Digite o novo nome do role</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="Nome do role" v-model="salaEdtNome"></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="red darken-1" text @click="dialog.EdtNomeSala = false;">Cancelar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="gravaNomeSala()">Confirmar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-content>
</template> 


<style scoped>
.c-name {
  position: fixed;
  width: 100%;
  top: 15px;
  color: white;
  float: left;
  right: 0;
  z-index: 30;
  padding-left: 50px;
  font-size: 1.2rem;
}
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
import config from "../assets/dados/config";
import axios from "axios";

export default {
  methods: {
    recalculaTotal() {
      //calcula total do role
      let vm = this;
      vm.totalDoRole = 0;
      console.log(this.items);
      this.items.map(a => (vm.totalDoRole += a.item.valor * a.quantidade));

      //calcula parcial do role
      vm.totalPessoal = 0;
      this.items.map(a =>
        a.perfil.map(perf => {
          vm.totalPessoal +=
            perf.id == vm.usuarioLogado.id ? a.item.valor * a.quantidade : 0;
        })
      );
    },
    novoItem() {
      this.AcaoItem = "Novo";
      this.itemSendoEditado = {
        id: null,
        item: {
          id: null,
          descricao: null,
          itemTipo: "OUTRO",
          valor: null
        },
        perfil: [],
        quantidade: 1
      };
      this.dialog.Edit = true;
    },
    salvarItem(item) {
      item.item.valor = parseFloat(item.item.valor) || 0;
      this.precoErro = item.item.valor <= 0;
      //valida pagantes
      this.selectErro = item.perfil.length <= 0;
      //valdia quantidade
      this.qtdErro = item.quantidade <= 0;

      if (!this.precoErro && !this.selectErro && !this.qtdErro) {
        this.precoErro = false;
        this.selectErro = false;
        this.qtdErro = false;

        let pagantesJson = [];

        item.perfil.map(function(pagante) {
          pagantesJson.push({
            id: pagante
          });
        });

        if (this.AcaoItem == "Novo") {
          pagantesJson = pagantesJson.map(a => {
            return { id: a.id };
          });
          let data = {
            id: this.sala.id,
            pedido: [
              {
                item: {
                  descricao: item.item.descricao + "",
                  itemTipo: item.item.itemTipo,
                  valor: item.item.valor
                },
                perfil: pagantesJson,
                quantidade: item.quantidade
              }
            ]
          };
          console.log(data);
          axios
            .post(`http://${config.api.host}${config.api.port}/pedidos`, data, {
              auth: config.api.auth
            })
            .then(
              response => {
                if (response.data) this.items = response.data;
                console.log(response);
              },
              error => {
                console.log(error);
              }
            );
          this.atualizaJson();
          return true;
        } else if (this.AcaoItem == "Editar") {
          pagantesJson = pagantesJson.map(a => {
            return { id: a.id.id };
          });
          let data = {
            id: item.id,
            item: {
              id: item.item.id,
              descricao: item.item.descricao + "",
              itemTipo: item.item.itemTipo,
              valor: item.item.valor
            },
            perfil: pagantesJson,
            quantidade: item.quantidade
          };

          console.log(data);
          axios
            .put(
              `http://${config.api.host}${config.api.port}/pedidos/${data.id}`,
              data,
              { auth: config.api.auth }
            )
            .then(
              response => {
                console.log(response.data);
              },
              error => {
                console.log(error);
              }
            );
          this.atualizaJson();
          return true;
        }
      } else {
        return false;
      }
    },
    excluirItem(item) {
      console.log("excluir");

      console.log(item.id);
      let alteraIndex = this.items.findIndex(x => x.id === item.id);
      this.items.splice(alteraIndex, 1);

      axios
        .delete(
          `http://${config.api.host}${config.api.port}/pedidos/${item.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            console.log(response.data);
            this.recalculaTotal();
          },
          error => {
            console.log(error);
          }
        );

      //no response atualizar itens
      //this.recalculaTotal();
    },
    editarItem(item) {
      console.log(item);

      this.AcaoItem = "Editar";
      this.itemSendoEditado = { ...item };
      this.dialog.Edit = true;
    },
    verificaPreco() {
      console.log("verifica");

      if (this.itemSendoEditado.item.valor.length <= 0) this.precoErro = true;
      else {
        let preco = (this.itemSendoEditado.item.valor + "").replace(/\,/g, ".");
        preco = preco.includes(".") ? preco : preco + ".00";
        preco = preco.replace(/[^0-9.]/g, "");
        preco = preco.split(".");
        preco = preco.slice(0, -1).join("") + "." + preco.slice(-1);
        this.itemSendoEditado.item.valor = parseFloat(preco).toFixed(2);
        this.precoErro = false;
      }
    },
    fecharRoleParcial() {
      console.log("REQUISIÇÃO POST PARA BACKEND (FECHAR ROLE)");
      this.dialog.FechaItem = true;
      this.dialog.FechaParcial = false;
      //no response exibir o total
    },
    fecharRole() {
      console.log("a");
    },
    gravaNomeSala() {
      //faz nome sala
      axios
        .get(
          `http://${config.api.host}${config.api.port}/pedidos/salas/${this.sala.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            this.items = response.data ? response.data : [];
          },
          error => {
            console.log(error.data);
          }
        );
    },
    toggleSelect() {
      this.itemSendoEditado.perfil = this.pessoas.map(a => a.id);
    },
    atualizaJson() {
      axios
        .get(
          `http://${config.api.host}${config.api.port}/pedidos/salas/${this.sala.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            this.items = response.data ? response.data : [];
            this.recalculaTotal();
          },
          error => {
            console.log(error.data);
          }
        );

      axios
        .get(
          `http://${config.api.host}${config.api.port}/salas/${this.sala.id}/usuarios`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            this.pessoas = response.data;
            this.pessoasPerfil = this.pessoas.map(p => {
              return p.perfil;
            });
          },
          error => {
            console.log(error);
          }
        );
    },
    localizaSalaUsuario() {
      axios
        .get(
          `http://${config.api.host}${config.api.port}/historicos/usuarios/${this.usuarioLogado.id}`,
          { auth: config.api.auth }
        )
        .then(
          response => {
            if (response.data) {
              this.sala = response.data.filter(obj => {
                return !obj.data_saida;
              })[0].sala;
              this.atualizaJson();
            }
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
      config: config,
      totalPessoal: 0,
      totalDoRole: 0,
      sala: {
        codigo: null,
        id: null,
        nome: null
      },
      precoErro: false,
      selectErro: false,
      qtdErro: false,
      dialog: {
        Edit: false,
        Excluir: false,
        FechaParcial: false,
        FechaSala: false,
        CodSala: false,
        FechaItem: false,
        EdtNomeSala: false
      },
      dialogMsg: "",
      AcaoItem: "Editar",
      salaEdtNome: "",
      fotoDefault:
        "https://firebasestorage.googleapis.com/v0/b/i-role.appspot.com/o/src%2Ffoto-padrao.png?alt=media&token=7899e09b-3157-4c49-a18c-66ab3f20d067",
      itemSendoEditado: {
        id: null,
        item: {
          id: null,
          descricao: null,
          itemTipo: null,
          valor: null
        },
        perfil: [1],
        quantidade: null
      },
      items: [],
      pessoas: [],
      pessoasPerfil: [],
      usuarioLogado: JSON.parse(localStorage.getItem("User")),
      intervaloAtualiza: setInterval(() => {
        this.atualizaJson();
      }, 100000000000)
    };
  },
  mounted() {
    this.localizaSalaUsuario();
  }
};
</script>