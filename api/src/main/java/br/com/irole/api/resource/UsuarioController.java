package br.com.irole.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.UsuarioRepository;
import br.com.irole.api.service.PerfilService;
import br.com.irole.api.service.UsuarioService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PerfilService perfilService;
	
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private BCryptPasswordEncoder codifica;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	@ApiOperation(notes = "Lista todos os usuários cadastrados no sistema", value = "Listar usuários")
	public ResponseEntity<?> listarUsuarios(){
		List<Usuario> usuarios = usuarioRepository.findAll();
		return !usuarios.isEmpty() ? ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@ApiOperation(notes = "Cadastrar um novo usuário passando o objeto Usuário no corpo da requisição", value = "Registra usuário")
	public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario, HttpServletResponse response){
		List<Erro> erros = new ArrayList<>(); 
		
		usuario.setSenha(codifica.encode(usuario.getSenha()));
		Usuario novoUsuario = null;
		try {
			novoUsuario = usuarioRepository.save(usuario);
			publisher.publishEvent(new RecursoCriadoEvent(this, response, novoUsuario.getId()));
			
		} catch (Exception e) {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario.jaexiste", null,LocaleContextHolder.getLocale());
			String mensagemDev = e.getMessage();
			Erro erro = new Erro(mensagemUsuario, mensagemDev);
			erros.add(erro);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(notes = "Busca usuário pelo id do usuário", value = "Busca usuário")
	public ResponseEntity<Usuario> buscaUsuarioId(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{id}/perfil")
	@ApiOperation(notes = "Busca o perfil de um determinado usuário, passando ID do usuário na URI", value = "Retornar perfil do usuário")
	public ResponseEntity<?> buscaPerfilId(@PathVariable Long id) {
		return perfilService.buscaPerfilId(id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(notes = "Setar a flag do usuário para inativo", value = "Deletar usuário")	
	public void removerUsuario(@PathVariable Long id) {
		usuarioService.atualizarAtivo(id, false);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(notes = "Altera dados do usuário, passando objeto Usuário no corpo e a ID do usuário pela URI", value = "Altera usuário")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
			Usuario usuarioSalvo = usuarioService.atualizar(id, usuario);
			return ResponseEntity.ok(usuarioSalvo);
		
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(notes = "Ativa ou desativa a conta do usuário via booleano (true/false) no corpo da requisição ", value = "Ativar/Desativar usuário")
	public void atualizaAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
		usuarioService.atualizarAtivo(id, ativo);
	}
	
	
	
	
	
	
	
	
}
