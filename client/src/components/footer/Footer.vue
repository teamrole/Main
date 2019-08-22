<template>
    <div class="footer">
        
        <div class="social">
            <p1f-social></p1f-social>
        </div>  

        <nav class="footer-menu">
            <ul class="items">              
                    <li><a href="#">Home</a></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Info</a></li>                    
                    <li><a href="#">Pricing</a></li>
                    <li><a href="#">Contact</a></li>     
                    <li><a href="#">Sitemap</a></li>        
            </ul>
        </nav>
                  
        <div class="footer-logo">
            <p1f-logo></p1f-logo>
        </div>  

        <div class="terms">
            <div class="terms-wrapper">
                <a href="#">Privacy policy</a>
                <a href="#">Terms and coditions</a>
            </div>
        </div>

        <div class="scrollup">Go to the top of the page</div>
    </div>
</template>

<script>
import Social from '../social/Social.vue';
import Logo2 from '../../header/logo/Logo.vue';

export default {
    components:{
        'p1f-logo': Logo2,
        'p1f-social' : Social
    }
}
</script>

<style scoped>
.footer > div{
    margin-bottom: 30px;
}

.footer > div:last-of-type{
    margin-bottom: 0;
}

.terms a{
    display: block;
    margin-bottom: .6em;
    text-decoration: underline
}

.footer-menu{display: none}

@media (min-width: 600px) {
    .scrollup{display: none}
    .footer-menu{display: initial}
    .footer > div{ margin-bottom: 0;}

    .footer{
        display: grid;
        grid-gap: 20px;
        grid-template-columns: repeat(12, 1fr);
        grid-template-areas: "so so so me me me te te te fl fl fl"; 
    }
    .social{
        grid-area: so;
    }
    .footer-menu{
        grid-area: me;              
        display: grid
    }
    .footer-menu li{
        max-width: 80px;
        word-wrap: break-word;
        text-align: center;
        margin-bottom: .8em
    }
    
    .footer-menu .items{
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
    }   
    .terms{
        grid-area: te;
        display: grid;
    }
    .footer-logo{
        grid-area: fl;
        display: grid;
    }    
    
}
</style>

