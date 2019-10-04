<template>
  <v-content>
    <v-container fill-height='100' .offset-lg-4>
      <v-row align='center' justify='center'>
        <v-col cols='12' sm='8' md='4' class='text-centered'>
          <v-text-field
            v-mask='mask'
            label='Insira seu número'
            color='primary'
            v-bind:outlined='true'
            v-bind:clearable='true'
            v-bind:hide-details='true'
            placeholder='+55(99)9 9999-9999'
          ></v-text-field>
          <v-btn
            class='btn-marger'
            color='primary'
            href='#'
            elevation='3'
            width='100%'
            v-bind:large='true'
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
  import { mask } from 'vue-the-mask'
  export default {
    directives: {
      mask,
    },
    data: () => ({
      mask: '+55 (##) #####-####',
      value: '4444444444444444',
    }),
  }
</script>