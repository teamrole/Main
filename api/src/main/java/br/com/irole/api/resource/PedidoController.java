package br.com.irole.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.service.PedidoService;
import io.swagger.annotations.ApiOperation;

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
	@ApiOperation(notes = "Retorna uma lista de pedidos pelo ID da sala (URI)", value = "Capturar pedidos")
	public ResponseEntity<List<Pedido>> listarPedido(@PathVariable Long id){
		Optional<Sala> salaOP = SalaRepository.findById(id);
		if(!salaOP.isPresent())
			return ResponseEntity.notFound().build();
		
		List<Pedido> pedidos = salaOP.get().getPedido();
		
		return !pedidos.isEmpty() ? ResponseEntity.ok(pedidos) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@ApiOperation(notes = "Cadastra um ou mais pedidos, passando um objeto Sala no corpo, contendo os pedidos", value = "Registrar pedidos")
	public ResponseEntity<?> cadastrar(	@Valid @RequestBody Sala sala, HttpServletResponse response){	
		
		return pedidoService.salvarPedido(sala);
		
	}
	
	@PutMapping("/{id}")
	@ApiOperation(notes = "Edita dados do pedido, passando um objeto Pedido no corpo e o ID do pedido à ser alterado via URI", value = "Edita Pedido")
	public Pedido atualizar(@PathVariable Long id, @Valid @RequestBody Pedido pedido){
			Pedido pedidoSalvo = pedidoService.atualizar(id, pedido);
			return pedidoSalvo;    
		
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(notes = "Apagar pedido pelo ID", value = "Apaga Pedido")
	public ResponseEntity<?> apagar(@PathVariable Long id) {
		return pedidoService.apagarPedido(id);
	}
}
