package br.com.irole.api.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PerfilRepository;

@RestController
@RequestMapping("/perfis")
public class PerfilController {
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private PerfilController perfilService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping
	public ResponseEntity<Perfil> cadastrarPerfil(@Valid @RequestBody Perfil perfil, HttpServletResponse response){
		Perfil novoPerfil = perfilRepository.save(perfil);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novoPerfil.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novoPerfil);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Perfil perfil){
			ResponseEntity<Usuario> usuarioSalvo = perfilService.atualizar(id, perfil);
			return usuarioSalvo;    
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
