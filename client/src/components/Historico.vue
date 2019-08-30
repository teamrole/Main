
<template>
  <v-content>
    <v-row>
      <v-col>
        <p class="text-center headline">Histórico de Rolês</p>
        <v-expansion-panels
        :flat='true'>
          <v-expansion-panel v-for="(item,i) in 15" :key="i">
            <v-expansion-panel-header>
              <div class="col-8"><v-icon>where_to_vote</v-icon> Boteco do Ceará </div>   
              <div class="col-4 grey--text"><v-icon>calendar_today</v-icon> 27/08/2019 </div>            
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <div class="col-12 d-inline-flex">
                <div class="col-4">
                  <v-icon>group</v-icon>     
                  
                  <!--<v-icon>shopping_cart</v-icon>
                   <v-icon>store</v-icon>-->

                <span class=' red--text font-weight-bold'> 4</span>
                </div>
                <div class="col-4">
                <span class='text-center red--text font-weight-bold'>
                  <v-icon>monetization_on</v-icon> 
                  57,39</span>
                </div>
                 <div class="col-4"> 
                  <span class='text-right red--text font-weight-bold'>               
                    <v-icon>bar_chart</v-icon>
                    292,07</span>
                </div>
              </div>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-col>
    </v-row>
  </v-content>
</template>
