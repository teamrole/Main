<template>
  <v-content>
    <v-container>
      <v-tabs v-model="tab" color="primary" grow>
        <v-tab v-for="item in items" :key="item.titulo" class="c-tab-header">{{ item.titulo }}</v-tab>
      </v-tabs>
      <h2>teste</h2>
    </v-container>
  </v-content>
</template> 


<script>
export default {
  data() {
    return { 
        items: [{
          titulo: "pessoa1",
          componente: "idpessoa"
        }]
        
    }
  }
};
</script>