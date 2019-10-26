<template>
  <v-app>
    <t-navbar />

    <RouterView />

    <t-footer />
  </v-app>
</template>

<script>
import navbar from "./components/Templates/navbar";
import footer from "./components/Templates/footer";

export default {
  name: "iRole",
  components: {
    "t-navbar": navbar,
    "t-footer": footer
  },
  props: {
    source: String
  },
  components: {
    "t-navbar": navbar,
    "t-footer": footer
  }
};
</script>
