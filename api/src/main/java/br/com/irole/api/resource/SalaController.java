package br.com.irole.api.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
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
	public Sala buscaSalaId(@PathVariable Long id) {
		return salaService.buscaSala(id);
	}
	
	@PostMapping
	public ResponseEntity<Sala> criarSala(HttpServletResponse response){
		Sala novaSala = new Sala();
		novaSala.setCodigo(RandomStringUtils.randomAlphanumeric(5));
		salaRepository.save(novaSala);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaSala.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
	}
	
	@PutMapping("/entrar/{idU}/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entrarSala(@PathVariable String codigo, @PathVariable Long idU) {
		salaService.entraSala(idU,codigo);
	}
		
	
	@PutMapping("/{id}/fechar/{idU}")
	public Sala buscaSalaId(@PathVariable Long id, @PathVariable Long idU) {
		return salaService.fecharContaUsuario(id, idU);
	}	
		
	@PutMapping("/{id}/fechar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechaSala(@PathVariable Long id) {
		salaService.fecharSala(id);
	}
}
