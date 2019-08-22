import Home from './components/pages/home/HomeTemplate.vue';
import Imoveis from './components/pages/imoveis/Imoveis.vue';
import Regioes from './components/pages/regioes/RegiaoList.vue';
import Consulta from './components/pages/consulta/Consulta.vue';
import NovoImovel from './components/pages/novoImovel/ImovelForm.vue';

export const routes = [
    {path: '', component: Home},
    {path: '/imoveis', component: Imoveis},
    {path: '/regioes', component:Regioes},
    {path: '/consulta', component: Consulta},
    {path: '/contato', component: NovoImovel}
];