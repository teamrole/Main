<template>
    <nav role="navigation">
        <div id="menuToggle">
            <span></span>
            <span></span>
            <span></span>    
        
            <ul id="menu">
                <div class="nav-auth">
                  <div>
                  <li><a href="#">Register</a></li> 
                  <li><a href="#">Login</a></li>
                  </div>
                </div>         
                <div class="nav-items">  
                  <li><a href="#">Home</a></li>
                  <li><a href="./#/consulta">Consultar Valor</a></li>
                  <li><a href="./#/imoveis">Imóveis</a></li>
                  <li><a href="./#/regioes">Bairros</a></li>
                  <li><a href="./#/contato">Cadastre imóvel</a></li>                  
                </div>             
            </ul>
        </div>
    </nav>
</template>

<script>
export default {
    
}
</script>

<style scoped>
nav{
  width: 100%;
  position: relative;
  text-align: left;
  height: auto;
}
#menuToggle
{
  width: 100%; 
  display: block;
  position: relative;
  top: 3px; 
  z-index: 1;
  box-sizing: border-box;  
  -webkit-user-select: none;
  user-select: none;
}

#menuToggle input
{
  display: block;
  width: 40px;
  height: 32px;
  position: absolute;
  top: -7px;
  left: 50%;
  transform:translateX(-50%);
  
  cursor: pointer;
  
  opacity: 0; /* hide this */
  z-index: 2; /* and place it over the hamburger */
  
  -webkit-touch-callout: none;
}

/*
 * Just a quick hamburger
 */
#menuToggle span
{
  display: block;
  width: 33px;
  height: 4px;
  margin-bottom: 5px;
  position: relative;
  left: 50%;
  transform:translateX(-50%);
  
  background: #cdcdcd;
  border-radius: 3px;
  
  z-index: 1;
  
  transform-origin: 4px 0px;
  
  transition: transform 0.5s cubic-bezier(0.77,0.2,0.05,1.0),
              background 0.5s cubic-bezier(0.77,0.2,0.05,1.0),
              opacity 0.55s ease;
}

#menuToggle span:first-child
{
  transform-origin: 0% 0%;
}

#menuToggle span:nth-last-child(2)
{
  transform-origin: 0% 100%;
}

/* 
 * Transform all the slices of hamburger
 * into a crossmark.
 */
#menuToggle input:checked ~ span
{
  opacity: 1;
  transform: rotate(45deg) translate(-2px, -1px);
  background: #232323;
}

/*
 * But let's hide the middle one.
 */
#menuToggle input:checked ~ span:nth-last-child(3)
{
  opacity: 0;
  transform: rotate(0deg) scale(0.2, 0.2);
}

/*
 * Ohyeah and the last one should go the other direction
 */
#menuToggle input:checked ~ span:nth-last-child(2)
{
  transform: rotate(-45deg) translate(0, -1px);
}

/*
 * Make this absolute positioned
 * at the top of the screen
 */
#menu
{
  position: absolute;
  left:0;
  padding-top: 25px;
  list-style-type: none;
  -webkit-font-smoothing: antialiased;
  /* to stop flickering of text in safari */
  
  transform-origin: 0% 0%;
  transform: translate(0, -150%);

}

#menu li
{
  padding: 10px 0;
  font-size: 22px;
}

/*
 * And let's slide it in from the bottom
 */
#menuToggle input:checked ~ ul
{ 
  transition: transform 0.5s cubic-bezier(0.77,0.2,0.05,1.0);
  transform: none;
  position: static;
}
.nav-items{
  height: 100%;     
}

@media (min-width: 600px) {
  #menuToggle span, #menuToggle input{
    display: none
  }
  #menu{
    transform:none;
    padding-top: 0;
  }
  #menu li{
    padding: 0;
  } 
  #menu{
    position: relative;
    width: 100%;
    display: grid; 
    grid-template-columns: 1fr 1fr;
    grid-template-rows: 24px auto;
    grid-template-areas: 
    "logo auth"
    "menu menu";      
  }
  .nav-auth{
    grid-area: auth;
    text-align: center; 
  }  
  .nav-auth li{
    display: inline; 
    padding-bottom: 0;
  }
  .nav-items{
    grid-area: menu;
    display: grid; 
    grid-gap: 5px;
    margin-top:40px;
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
    grid-auto-rows: 24px;  
    text-align: center;
    justify-content: center
  }
  .nav-items > li{
    grid-column: auto;
    line-height: 24px; 
  }
}

@media (min-width: 900px) {
  #menu{
    grid-template-columns: 1fr;
    grid-template-rows: minmax(26px, max-content);
    grid-template-areas: ""; 
  }
  #menu, nav, #menuToggle{
    position:static
  }

  .nav-auth{
    position: absolute;
    top: 0;
    right: 120px;  
    line-height: 50px;             
    grid-area:initial;
    max-width: 100%;
    display: grid;
    grid-column-gap: 20px;
  }

  .nav-auth li{
    grid-row: 1
  }

  .nav-items{
    margin-top: 0 ;
    position: relative;
    grid-row: 1;
    grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
    grid-auto-rows:26px;
    text-align: right;
  }
  .nav-items > li{
    grid-row: auto;
  }
}

@media (min-width: 1440px) { 
  .nav-auth{
    right: unset;
    width: 100%;
    left: 0;
    grid-template-columns: 1fr 1200px 1fr;
  }  

  .nav-auth > div{
    grid-column: 2;
    justify-self: right
  }
  
}

</style>

