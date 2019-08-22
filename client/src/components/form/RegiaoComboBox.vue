<template>
    <div>
        <div>
            <label for="estado">Estado:</label>
            <select v-model="estado" id="estado" @change="estadoInput($event)" >
                <option value="">Selecione um estado</option>
                <option v-for="e in estados" :key="e.id" :value="e.id">{{ e.nome }}</option>
            </select>
        </div>
    
        <div>
            <label for="cidade">Cidade:</label>                             
            <select v-model="cidade" id="cidade" :disabled="disable_cidade"
                @change="cidadeInput($event)" > 
                <option value="">Selecione uma cidade</option>
                <option v-for="c in select_cidade" :key="c.id" :value="c.id">{{ c.nome }}</option>
            </select> 
        </div>  

        <div>
            <label for="bairro">Bairro:</label>                             
            <select v-model="bairro" id="bairro" :disabled="disable_bairro"
             @change="bairro_selecionado($event)" > 
                <option value="">Selecione um bairro</option>
                <option v-for="b in select_bairro" :key="b.id" :value="b.id">{{ b.nome }}</option>
            </select> 
        </div> 
    </div>
</template>

<script>
import axios from 'axios';
export default {   
    data(){
        return{
            estados: [],
            select_cidade: [],
            select_bairro: [],            
            disable_cidade: '',
            disable_bairro: '',   
            select_bairro: '',

            estado: '',
            cidade: '',            
            bairro: '',
        }
    },
     methods: {   
         
         lerEstados() {
        axios.get('http://localhost:8000/api/v1/estados')
        .then(response => {           
            this.estados = response.data
        })
        .catch(error => {
            console.log(error);
        })
      },
      lerCidades($estado) {
        axios.get('http://localhost:8000/api/v1/estados/'+$estado+'/cidades')
        .then(response => {
            this.select_cidade = response.data;
            this.disable_cidade = false;       
        })
        .catch(error => {
            console.log(error);
        })
      },
      lerBairros($cidade) {
        axios.get('http://localhost:8000/api/v1/cidades/'+$cidade+'/bairros')
        .then(response => {
            this.select_bairro = response.data;
            this.disable_bairro = false;               
        })
        .catch(error => {
            console.log(error);
        })
      },
      estadoInput($event){     
          let inputValue = $event.currentTarget.value;         
          this.lerCidades(inputValue);          
      },
      cidadeInput($event){     
          let inputValue = $event.currentTarget.value;         
          this.lerBairros(inputValue);          
      },
      bairro_selecionado($event){
        let inputValue = $event.currentTarget.value;
        this.$emit('bairro_selecionado', inputValue);
      }
     },
      created() {
        this.lerEstados();        
        this.disable_cidade = true;
        this.disable_bairro = true;
    }
}
</script>

<style>

</style>
