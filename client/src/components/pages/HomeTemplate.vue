<template>
    <div class="container">  
       
        <div class="header">
            <div class="logo">
                <t-logo></t-logo>
            </div>
            <div class="menu">
                <p1-menu></p1-menu>
            </div>
        </div>

        <p1-chamada></p1-chamada>

        <main>
            <p1-content></p1-content>

            <div class="features">
                <div class="feature-three-cols">
                    <p1-feature :src="ft[0]" :titulo="ft[1]" :texto="ft[2]"></p1-feature>
                </div>
                <span class="separator"></span>
                <div class="feature-three-cols">
                    <p1-feature :src="ft2[0]" :titulo="ft2[1]" :texto="ft2[2]" class="invert"></p1-feature>
                </div>
                <span class="separator"></span>
                <div class="feature-three-cols">
                    <p1-feature :src="ft3[0]" :titulo="ft3[1]" :texto="ft3[2]"></p1-feature> 
                </div>                
            </div>

            <div class="assine">
                <p1-assine></p1-assine>
            </div>

        </main>

        <footer>
            <p1-footer></p1-footer>
        </footer>
         
    </div> 
</template>

<script>
import Menu from '../../header/menu/Menu.vue';
import Logo from '../../header/logo/Logo.vue';
import Feature from '../../main/feature/Feature.vue';
import Assine from '../../main/assine/Assine.vue';
import Footer from '../../footer/footer/Footer.vue';
import Chamada from '../../header/chamada/Chamada.vue';
import Content from '../../main/content/content.vue';

export default {    
    components:{
        'p1-menu': Menu,
        't-logo': Logo,
        'p1-feature' : Feature,
        'p1-assine' : Assine,
        'p1-footer' : Footer,
        'p1-chamada' : Chamada,
        'p1-content' : Content
    },
     data(){
        return {
            ft: ['/static/fedena/img/Computer_Icon.png', 'Feature 1', 'Maecenas commodo vulputate erat, id porttitor mi bibendum non.'+
                 'Ut nec nisl at felis eleifend accumsan at vel ante.'],
            
            ft2: ['/static/fedena/img/Code_Icon.png', 'Feature 2', 'Donec aliquam lacinia elit, auctor blandit lorem auctor quis.'],
           
            ft3: ['/static/fedena/img/Layers_Icon.png', 'Feature 3', 'Nullam ac mauris eu ante rhoncus condimentum.'+ 
            'Nam placerat neque at ipsum commodo, eget ultricies diam condimentum.']
        }
    }
}

</script>

<style>

.container{
    width: 100%;
    min-height: 100%;
    display: grid;
    grid-template-columns: 20px 1fr 20px;
    padding-top: 80px;
}

.container > div, .container > *{
  grid-column: 2;
  min-width: 0
}

.header{
    width: 100%;
    position: fixed;
    display: grid;
    grid-template-columns: 1fr 1fr;    
    padding:20px 0;
    grid-template-areas: "logo menu";
    max-height: 100%;
    overflow-y: auto
}

.logo{
    grid-area: logo;   
}

.logo a{
    width: 100%;    
}

.menu{
    grid-area: menu;   
    position: relative;    
}
.assine_left{
    width: 100%;
    height: auto;   
    box-sizing: border-box;
}

.assine_left ul li{
    display: inline-block
}

.assine_right{
    width: 100%;
    height: auto;   
    box-sizing: border-box;
}

.img-placeholder{
    background-color: #999;  
    margin: 0 auto;
}

.img-placeholder-m{   
    width: 225px;
    height: 175px;     
}

.img-placeholder-g{
    width: 100%;
    height: 260px; 
}
@media (min-width: 600px) {
    .container{
        grid-template-columns: 40px 1fr 40px;
        padding-top: 0;
    }
    
    .header{
        text-align: left;
        position:static;
        display:initial;
        padding:40px 0 !important;
        overflow: hidden;
    }

    .logo{
        display: initial;
        width: 50%;
        position:absolute;
        left: 0;
        padding-left: 40px;
        box-sizing: border-box
    }

    .img-placeholder-g{
        width: 80%;
    }
    
}

@media (min-width: 900px) {
    .container{
        grid-template-columns: 120px 1fr 120px;
       
    }
    .header{
        display: grid;
        grid-template-columns: repeat(12, 1fr);
        margin-top: 50px;
    }
    .menu{
        position: static;
        grid-column:  4 / 12 ;
        grid-auto-rows: 24px 24px;
    }
    .logo{
        grid-column: 1 / 3;
        position: initial;
        padding-left: 0;
        width: 100%;  
    }
    .features{
        display: grid;
        grid-gap: 10px;
        grid-template-columns: repeat(12, 1fr);  
        grid-template-areas: "l l l l m m m m r r r r";
    }
    .features .separator{
        display: none
    }
    .features .feature-three-cols:nth-of-type(1){
        grid-area: l;
    }
    .features .feature-three-cols:nth-of-type(2){
        grid-area: m;
    }
    .features .feature-three-cols:nth-of-type(3){
        grid-area: r;
    }
}

@media (min-width: 1440px) {
    .container{
        grid-template-columns: 1fr 1200px 1fr;
    }
}



</style>
