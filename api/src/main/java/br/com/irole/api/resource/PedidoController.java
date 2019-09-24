package br.com.irole.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PedidoRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private SalaRepository SalaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping("salas/{id}")
	public ResponseEntity<List<Pedido>> listarPedido(@PathVariable Long id){
		Optional<Sala> salaOP = SalaRepository.findById(id);
		if(!salaOP.isPresent())
			return ResponseEntity.notFound().build();
		
		List<Pedido> pedidos = salaOP.get().getPedido();
		
		return !pedidos.isEmpty() ? ResponseEntity.ok(pedidos) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<List<Pedido>> cadastrar(@RequestBody Sala sala, HttpServletResponse response){	
		List<Pedido> novoPedido = pedidoService.salvarPedido(sala);
		
		for(Pedido pedido: novoPedido) {
			publisher.publishEvent(new RecursoCriadoEvent(this, response, pedido.getId()));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);		
	}
}
