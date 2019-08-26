<template>
  <v-app id="inspire">
    <t-navbar/>

    <v-content>
    </v-content>
  
    <t-footer/>
    
  </v-app>
</template>

<script>

import navbar from "./Templates/navbar"
import footer from "./Templates/footer"

  export default {
    props: {
      source: String,
    },
    components: {
      "t-navbar":navbar,
      "t-footer":footer
    }
  }
</script>