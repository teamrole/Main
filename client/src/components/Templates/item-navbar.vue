<template>
    <v-list-item @click="">
        <v-list-item-action>
        <v-icon>{{icon}}</v-icon>
        </v-list-item-action>
        <v-list-item-content>
        <v-list-item-title>{{text}}</v-list-item-title>
        </v-list-item-content>
    </v-list-item>
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
   href: {
    type: String,
    default: ''
   }
  }
}
</script>

