<template>
  <div class="c-avatar">
    <div class="c-block">
      <v-avatar size="40px">
        <v-img :src="avatar" />
      </v-avatar>
    </div>
    <div>
      <span>{{nome}}</span>
    </div>
  </div>
</template>

<script>
export default {
  
  props:
    ['nome', 'avatar']
}

</script>

<style scoped>
.c-avatar {
  font-size: 0.7em;
  display: inline-block;
  text-align: center;
}
.c-block {
  display: block;
}
</style>