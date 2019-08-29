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
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.UsuarioRepository;
import br.com.irole.api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<?> listarUsuarios(){
		List<Usuario> usuarios = usuarioRepository.findAll();
		return !usuarios.isEmpty() ? ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@Valid @RequestBody Usuario usuario, HttpServletResponse response){
		Usuario novoUsuario = usuarioRepository.save(usuario);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novoUsuario.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> buscaUsuarioId(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
			Usuario usuarioSalvo = usuarioService.atualizar(id, usuario);
			return ResponseEntity.ok(usuarioSalvo);
		
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizaAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		usuarioService.atualizarAtivo(id, ativo);
	}
	
	
	
	
	
	
	
	
}
