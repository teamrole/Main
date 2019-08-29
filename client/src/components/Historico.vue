<template>
    <v-content>
        <v-container> 
            <v-data-table
                :headers="headers"
                :items="desserts"
                :options.sync="options"
                :server-items-length="totalDesserts"
                :loading="loading"
                :mobile-breakpoint=800
                item-key="data"
                class="elevation-1 tableDef"
            ></v-data-table>
        </v-container>
    </v-content>
</template>


<script>
export default {
    data () {
    return {
      totalDesserts: 0,
      desserts: [],
      loading: true,
      options: {},
      headers: [
        {
          text: 'Data',
          align: 'left',
          sortable: false,
          value: 'data',
        },
        { text: 'Rolê', value: 'role' },
        {text: 'Número de Participantes', value: 'participantes'},
        { text: 'Valor Individual', value: 'vIndividual' },
        { text: 'Valor Total', value: 'vTotal' },
      ],
    }
  },
  watch: {
    options: {
      handler () {
        this.getDataFromApi()
          .then(data => {
            this.desserts = data.items
            this.totalDesserts = data.total
          })
      },
      deep: true,
    },
  },
  mounted () {
    this.getDataFromApi()
      .then(data => {
        this.desserts = data.items
        this.totalDesserts = data.total
      })
  },
  methods: {
    getDataFromApi () {
      this.loading = true
      return new Promise((resolve, reject) => {
        const { sortBy, descending, page, itemsPerPage } = this.options

        let items = this.getDesserts()
        const total = items.length

        if (this.options.sortBy) {
          items = items.sort((a, b) => {
            const sortA = a[sortBy]
            const sortB = b[sortBy]

            if (descending) {
              if (sortA < sortB) return 1
              if (sortA > sortB) return -1
              return 0
            } else {
              if (sortA < sortB) return -1
              if (sortA > sortB) return 1
              return 0
            }
          })
        }

        if (itemsPerPage > 0) {
          items = items.slice((page - 1) * itemsPerPage, page * itemsPerPage)
        }

        setTimeout(() => {
          this.loading = false
          resolve({
            items,
            total,
          })
        }, 1000)
      })
    },
    getDesserts () {
      return [
        {
          data: '25/08-2019',
          role: 'Boteco do Gaúcho',
          participantes: 4,
          vIndividual: 62.89,
          vTotal: 246.57
        },
        {
          data: '25/08-2019',
          role: 'Adega Porto',
          participantes: 5,
          vIndividual: 62.89,
          vTotal: 246.57
        },
        {
          data: '25/08-2019',
          role: 'Boteco do Gaúcho',
          participantes: 9,
          vIndividual: 89.89,
          vTotal: 488.89
        },
        {
          data: '27/08-2019',
          role: 'Chopão',
          participantes: 3,
          vIndividual: 39.89,
          vTotal: 156.66
        },
        {
          data: '29/08-2019',
          role: 'Bar do Ceará',
          participantes: 4,
          vIndividual: 77.89,
          vTotal: 280.57
        }
      ]
    },
  },
}
</script>
<style scoped>
  .tableDef{
  }
</style>