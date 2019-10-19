package br.com.irole.api.resource;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.service.SalaService;

@RestController
@RequestMapping("/salas")
public class SalaController {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private SalaService salaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping("/{id}")
	public ResponseEntity<Sala> buscaSalaId(@PathVariable Long id) {
		Sala sala = salaService.buscaSala(id);
		
		if(sala == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(sala);
	}
	
	@PostMapping
	public ResponseEntity<Sala> criarSala(HttpServletResponse response){
		Sala novaSala = new Sala();
		novaSala.setCodigo(RandomStringUtils.randomAlphanumeric(4));
		salaRepository.save(novaSala);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaSala.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
	}
	
	@PutMapping("/entrar/{idU}/{codigo}")
	public ResponseEntity<?> entrarSala(@PathVariable Long idU, @PathVariable String codigo,
			HttpServletResponse response) {
		
		return salaService.entraSala(idU,codigo);
	}
	
	@GetMapping("/{id}/{idU}/fecharConta")
	public BigDecimal fecharUsuario(@PathVariable Long id, @PathVariable Long idU) {
		 BigDecimal totalParcial = salaService.fecharParcial(id, idU);		 
		 return totalParcial;
	}	
		
	
	@GetMapping("/{id}/{idU}/contaUsuario")
	public BigDecimal contaUsuario(@PathVariable Long id, @PathVariable Long idU) {
		BigDecimal totalParcial = salaService.contaParcial(id, idU);
		return totalParcial;
	}	
		
	@PutMapping("/{id}/fechar")
	@ResponseStatus(HttpStatus.OK)
	public void fechaSala(@PathVariable Long id) {
		salaService.fecharSala(id);
	}
}