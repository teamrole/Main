package br.com.irole.api.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.event.RecursoCriadoEvent;
import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.service.SalaService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/salas")
public class SalaController {
	
	@Autowired
	private SalaRepository salaRepository;
	
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
	@ApiOperation(notes = "Cria uma nova sala e gera um código. Nenhum parâmetro é necessário", value = "Criar Sala")
	public ResponseEntity<Sala> criarSala(HttpServletResponse response){
		Sala novaSala = new Sala();
		novaSala.setCodigo(RandomStringUtils.randomAlphanumeric(4));
		salaRepository.save(novaSala);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaSala.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
	}
	
	@PostMapping("/entrar/{idU}/{codigo}")
	@ApiOperation(notes = "Entra numa sala usando o código/QR Code como parâmetro URI", value = "Entrar na sala")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "idU", value = "ID do usuário", required = true, dataType = "Long", paramType="path"),
		@ApiImplicitParam(name = "codigo", value = "Código criado quando a sala é gerada", required = true, dataType = "String", paramType="path")
	})	
	public ResponseEntity<?> entrarSala(
			@PathVariable Long idU, 
		    @PathVariable String codigo,
			HttpServletResponse response) {
		
		return salaService.entraSala(idU,codigo);
	}
	
	@PostMapping("/{id}/{idU}/fecharConta")
	@ApiOperation(notes = "Fecha a conta de um usuário específico; ID da sala; ID do usuário na URI", value = "Fechar conta de um usuário")
	public BigDecimal fecharUsuario(@PathVariable Long id, @PathVariable Long idU) {
		 BigDecimal totalParcial = salaService.fecharParcial(id, idU);		 
		 return totalParcial;
	}	
	
	@GetMapping("/{id}/usuarios")
	@ApiOperation(notes = "Mostra todos os usários cadastrados numa sala, ID da Sala na URI", value = "Lista usuários da sala")
	public ResponseEntity<List<Usuario>> usuariosSala(@PathVariable Long id){
		List<HistoricoSalaUsuario> salas = historicoRepository.findByIDSala(id);
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for(HistoricoSalaUsuario historico : salas ) {
			usuarios.add(historico.getUsuario());
		}
		return !usuarios.isEmpty() ? ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}
		
	
	@GetMapping("/{id}/{idU}/contaUsuario")
	@ApiOperation(notes = "Retorna os gastos de um usuário específico dentro de uma sala", value = "Retorna Conta do usuário")
	public BigDecimal contaUsuario(@PathVariable Long id, @PathVariable Long idU) {
		BigDecimal totalParcial = salaService.contaParcial(id, idU);
		return totalParcial;
	}	
		
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(notes = "Troca a flag da sala para fechada", value = "Fechar sala")
	public void fechaSala(@PathVariable Long id) {
		salaService.fecharSala(id);
	}
}
