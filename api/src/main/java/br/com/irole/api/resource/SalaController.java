package br.com.irole.api.resource;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import br.com.irole.api.exceptionhandler.AppException;
import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.service.SalaService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/salas")
public class SalaController {
		
	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private SalaService salaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping("/{id}")
	@ApiOperation(notes = "Busca sala pelo ID", value = "Busca sala")
	public ResponseEntity<Sala> buscaSalaId(@PathVariable Long id) {
		Sala sala = salaService.buscaSala(id);
		
		if(sala == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(sala);
	}
	
	@PostMapping
	@ApiOperation(notes = "Cria uma nova sala e gera um código", value = "Criar Sala")
	public ResponseEntity<?> criarSala(@RequestBody  Perfil perfil, HttpServletResponse response){
		Sala novaSala;
		try {
			novaSala = salaService.criarSala(perfil);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new AppException(e.getMessage(), e.getMessage()));
		}
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaSala.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
	}
	
	@PostMapping("/{codigo}/{idPerfil}")
	@ApiOperation(notes = "Entra numa sala usando o código/QR Code como parâmetro URI", value = "Entrar na sala")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codigo", value = "Código criado quando a sala é gerada", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "idPerfil", value = "ID do Perfil", required = true, dataType = "Long", paramType="path")
		
	})	  
	public ResponseEntity<?> entrarSala(@PathVariable String codigo,@PathVariable Long idPerfil,HttpServletResponse response) {
		return salaService.entraSala(idPerfil,codigo);
	}
	
	@DeleteMapping("/{id}/{idPerfil}")
	@ApiOperation(notes = "Fecha a conta de um usuário específico; ID da sala; ID do perfil na URI", value = "Fechar conta de um usuário")
	public BigDecimal fecharUsuario(@PathVariable Long id, @PathVariable Long idPerfil) {
		 BigDecimal totalParcial = salaService.fecharContaDoUsuario(id, idPerfil);		 
		 return totalParcial;
	}	
	
	@GetMapping("/{id}/usuarios")
	@ApiOperation(notes = "Mostra todos os usários ATIVOS numa sala, passar o ID da Sala na URI", value = "Lista usuários da sala")
	public ResponseEntity<List<HistoricoSalaUsuario>> usuariosSala(@PathVariable Long id){
		List<HistoricoSalaUsuario> usuarios = historicoRepository.findUsuariosAtivo(id);
		
		return !usuarios.isEmpty() ? ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}/{idP}/conta")
	@ApiOperation(notes = "Retorna os gastos de um usuário específico dentro de uma sala, como parâmetro é esperado o id da Sala e o ID do perfil do usuário",
	value = "Retorna Conta do usuário")
	public BigDecimal contaUsuario(@PathVariable Long id, @PathVariable Long idP) {
		BigDecimal totalParcial = salaService.pegaContaDeUmUsuario(id, idP);
		return totalParcial;
	}	
		
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(notes = "Troca a flag da sala para fechada", value = "Fechar sala")
	public void fechaSala(@PathVariable Long id) {
		salaService.fecharSala(id);
	}
	
	@PutMapping("/{id}/nome")
	@ApiOperation(notes = "Editar o nome da sala passando o ID da sala como parãmetro e o nome no corpo; *Sem aspas", value = "Trocar nome da Sala")
	public ResponseEntity<?> editarNomeSala(@PathVariable Long id, @RequestBody String nome){
		return salaService.editarNomeSala(id, nome);
	}
}
