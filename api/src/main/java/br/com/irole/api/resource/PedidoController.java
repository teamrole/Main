package br.com.irole.api.resource;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping
	public ResponseEntity<Pedido> cadastrar(@RequestBody Pedido pedido, @RequestBody Sala sala, HttpServletResponse response){	
		Pedido novoPedido = pedidoService.salvarPedido(sala, pedido);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novoPedido.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);		
		
	}
}
