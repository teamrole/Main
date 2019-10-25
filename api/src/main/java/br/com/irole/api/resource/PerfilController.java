package br.com.irole.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.service.PerfilService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/perfis")
public class PerfilController {
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping
	@ApiOperation(notes = "Cadastra perfil passando um objeto Perfil no corpo", value = "Registra Perfil")
	public ResponseEntity<?> cadastrarPerfil(@Valid @RequestBody Perfil perfil, HttpServletResponse response){
		
		if (perfilRepository.findByUsuario(perfil.getUsuario()) == null) {
			Perfil novoPerfil = perfilRepository.save(perfil);
			publisher.publishEvent(new RecursoCriadoEvent(this, response, novoPerfil.getId()));
			return ResponseEntity.status(HttpStatus.CREATED).body(novoPerfil);
		}
		String mensagemUsuario = messageSource.getMessage("recurso.usuario-tem-perfil", null, LocaleContextHolder.getLocale());
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);	
	}
	
	@PutMapping("/{id}")
	@ApiOperation(notes = "Edita dados do perfil, passando um objeto Perfil no corpo e o ID do perfil à ser alterado via URI", value = "Edita Perfil")
	public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody Perfil perfil){
			Usuario usuarioSalvo = perfilService.atualizar(id, perfil);
			return usuarioSalvo;    
		
	}
	
	@GetMapping("/{id}/sala-atual")
	@ApiOperation(notes = "Mostra sala atual do usuario", value = "Sala do Usuario")
	public ResponseEntity<Sala> salaAtual(@PathVariable Long id){
		HistoricoSalaUsuario sala = historicoRepository.findSalaAtiva(id);
		Sala ativa = sala.getSala();
		return ResponseEntity.ok().body(ativa);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
