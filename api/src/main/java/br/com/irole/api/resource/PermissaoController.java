package br.com.irole.api.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.Permissao;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PermissaoRepository;
import br.com.irole.api.repository.UsuarioRepository;
import br.com.irole.api.repository.carga.CargaPeloId;
import br.com.irole.api.repository.customizado.IUsuarioPermissao;
import br.com.irole.api.repository.customizado.IUsuarioPermissao.UsuarioPermissao;
import io.swagger.annotations.ApiOperation;

@RequestMapping("permissoes")
@RestController
public class PermissaoController {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private IUsuarioPermissao iUsuarioPermissao;
	
	@GetMapping
	@ApiOperation(notes = "Lista todas as permissões cadastrada no sistema", value = "Lista Permissões")
	public ResponseEntity<List<Permissao>> listarPermissoes() {
			
		List<Permissao> permissoes = permissaoRepository.findAll();
				
		return (permissoes != null) ? ResponseEntity.ok(permissoes) : ResponseEntity.noContent().build() ;
	}
	
	@GetMapping("/{id}")
	@ApiOperation(notes = "Busca permissão usando ID Permissão na URI", value = "Busca Permissões")
	public ResponseEntity<Permissao> buscarPermissao(@PathVariable Long id){
		Optional<Permissao> permissaoOP = permissaoRepository.findById(id);
		if(permissaoOP.isPresent()) {
			return ResponseEntity.ok(permissaoOP.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("usuarios/{id}")
	@ApiOperation(notes = "Lista todas as permissões de um usuário (ID do usuário na URI)", value = "Lista Permissões do usuário")
	public ResponseEntity<List<Permissao>> permissaoDoUsuario(@PathVariable Long id){
		List<Permissao> permissoesDoUsuario = iUsuarioPermissao.findPermissaoByUsuarioID(id);
		
		return (permissoesDoUsuario != null) 
				? ResponseEntity.ok(permissoesDoUsuario) : ResponseEntity.noContent().build() ;
	}
	
	
	@PostMapping
	@ApiOperation(notes = "Cria uma nova permissão", value = "Registra uma Permissão")
	public ResponseEntity<Permissao> salvarPermissao(@Valid @RequestBody Permissao permissao, 
			HttpServletResponse httpServletResponse){
		
		Permissao permissaoSalva = permissaoRepository.save(permissao);
		publisher.publishEvent(new RecursoCriadoEvent(this, httpServletResponse, permissaoSalva.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(permissaoSalva);
	}
	
	//FIXME: Tratar exceção para quando o usuário já possui a permissão (Duplicate entry for key 'primary')
	@PostMapping("usuarios/{id}")
	@ApiOperation(notes = "Cadastra várias permissões para um usuário de uma vez, no corpo é recebido uma lista de ID de permissão", value = "Carga Permissão Usuário")
	public ResponseEntity<List<Permissao>> cargaPermissaoParaUsuario(@PathVariable Long id,
			@RequestBody CargaPeloId carga, HttpServletResponse response){
		
		/*TODO: refatorar pois atualmente seleciona usuario(e perfil), permissoes, deleta todas as permissoes 
		do usuario, e insere novamente todas as permissoes (tanto as antigas quanto as novas).*/
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		if(!usuario.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		List<Permissao> permissoes = carga.getId()
			.stream()
			.map( s -> {
				Permissao permissao = new Permissao();
				permissao.setId(s);
				return permissao;
			}).collect(Collectors.toList());
				
		usuario.get().getPermissao().addAll(permissoes);
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario.get());
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getId()));
		
		List<Permissao> permissoesDoUsuario = iUsuarioPermissao.findPermissaoByUsuarioID(id);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(permissoesDoUsuario);
	}
	
	//FIXME: Tratar exceção para quando o usuário já possui a permissão (Duplicate entry for key 'primary')
	@PostMapping("{id}/usuarios/{u}")
	@ApiOperation(notes = "Insere UMA permissão para um usuário, ID da permissão e ID do usuário na URI", value = "Atribuí uma permissão ao usuário")
	public ResponseEntity<UsuarioPermissao> usuarioRecebeUmaPermissao(@PathVariable Long id,
			@PathVariable Long u , HttpServletResponse response){
		
		/*TODO: refatorar pois atualmente seleciona usuario(e perfil), permissoes, deleta todas as permissoes 
		do usuario, e insere novamente todas as permissoes (tanto as antigas quanto as novas).*/
		
		Optional<Usuario> usuarioOP = usuarioRepository.findById(u);
		Optional<Permissao> permissaoOP = permissaoRepository.findById(id);
		
		if(!usuarioOP.isPresent())
			return ResponseEntity.notFound().build();
		
		if(!permissaoOP.isPresent())
			return ResponseEntity.notFound().build();	

		usuarioOP.get().getPermissao().add(permissaoOP.get());		
		usuarioRepository.save(usuarioOP.get());
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, permissaoOP.get().getId()));
		
		UsuarioPermissao usuarioPermissao = iUsuarioPermissao.findUsuarioPermissaoByUserAndPermissao(u, id);

		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioPermissao);
	}	
	
	@Transactional
	@DeleteMapping("{id}/usuarios/{user_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(notes = "Remove uma permissão do usuário,  ID da permissão e ID do usuário na URI", value = "Remove  Permissão do usuário")
	public void remove(@PathVariable Long id, @PathVariable Long user_id) {
				
		iUsuarioPermissao.deleteByUserAndPermissao(user_id, id);
	}
}
