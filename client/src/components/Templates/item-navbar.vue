<template>
  <router-link :to="goto" class="c-decoration-none">
    <v-list-item @click.stop color="primary">
      <v-list-item-action>
        <v-icon>{{icon}}</v-icon>
      </v-list-item-action>
      <v-list-item-content>
        <v-list-item-title>{{text}}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </router-link>
</template>

<script>
export default {
  props: {
    text: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      required: true
    },
    goto: {
      type: String,
      default: ""
    }
  }
};
</script>

<style scoped>
.c-decoration-none{
  text-decoration: none;
}
</style>