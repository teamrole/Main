
<template>
  <v-content>
    <v-row>
      <v-col>
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels mobile-breakpoint="400" active-class="c-painelAtivo">
          <t-expansionPanel>

          </t-expansionPanel>
        </v-expansion-panels>
      </v-col>
    </v-row>
  </v-content>
</template>
<script>
  import expansionPanel from "./Templates/expansion-panel"
  export default {
  data(){
    return{
      mainDate: null
    }
  },
  components:{
    't-extensionPanel' : expansionPanel 
  },
  methods:{
    headerClick:(e)=>{
      // = "Rolê: 47,51";
      return null;
    }
  },
}
</script>
<style scoped>
.c-primary {
  color: gray;
}
.c-secondary {
  color: #9c27b0;
}
.c-painelAtivo > .v-expansion-panel-header,
.c-painelAtivo > .v-expansion-panel-content {
  background-color: #eee;
}
.c-painelAtivo > .v-expansion-panel-header .c-data {
  display: none;
}
</style>
