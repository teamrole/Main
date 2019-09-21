package br.com.irole.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import br.com.irole.api.model.Permissao;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PermissaoRepository;
import br.com.irole.api.repository.UsuarioRepository;

@RequestMapping("permissoes")
@RestController
public class PermissaoController {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<List<Permissao>> listarPermissoes() {
			
		List<Permissao> permissoes = permissaoRepository.findAll();
				
		return (permissoes != null) ? ResponseEntity.ok(permissoes) : ResponseEntity.noContent().build() ;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Permissao> buscarPermissao(@PathVariable Long id){
		Optional<Permissao> permissaoOP = permissaoRepository.findById(id);
		if(permissaoOP.isPresent()) {
			return ResponseEntity.ok(permissaoOP.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("usuarios/{id}")
	public ResponseEntity<List<Permissao>> permissaoDoUsuario(@PathVariable Long id){
		List<Permissao> permissoesDoUsuario = usuarioRepository.findPermissaoByUsuarioID(id);
		
		return (permissoesDoUsuario != null) 
				? ResponseEntity.ok(permissoesDoUsuario) : ResponseEntity.noContent().build() ;
	}
	
	
	@PostMapping
	public ResponseEntity<Permissao> salvarPermissao(@Valid @RequestBody Permissao permissao, 
			HttpServletResponse httpServletResponse){
		
		@Valid
		Permissao permissaoSalva = permissaoRepository.save(permissao);
		publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, permissaoSalva.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(permissaoSalva);
	}
	
	
	
	
}
