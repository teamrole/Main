import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import 'material-design-icons-iconfont/dist/material-design-icons.css';
Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'mdi',
    ico: 'mdiCash' 
  },
  theme:{
    themes:{
      light:{
          primary: '#9c27b0',
          secondary: '#ff5722',
          accent: '#00bcd4',
          error: '#f44336',
          warning: '#ffc107',
          info: '#2196f3',
          success: '#8bc34a'
      }
    },
    options: {
      customProperties: true,
    },
  }
});

import '../assets/style.css';

