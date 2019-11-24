package br.com.irole.api.resource;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.service.PerfilService;
import br.com.irole.api.service.UsuarioService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PutMapping("/{id}")
	@ApiOperation(notes = "Edita dados do perfil, passando um objeto Perfil no corpo e o ID do perfil à ser alterado via URI", value = "Edita Perfil")
	public Perfil atualizar(@PathVariable Long id, @Valid @RequestBody Perfil perfil){

		Perfil perfilSalvo = perfilService.atualizar(id, perfil);
		return perfilSalvo;   		
	}
	
	@GetMapping("/{id}/sala-atual")
	@ApiOperation(notes = "Mostra sala atual do usuario", value = "Sala do Usuario")
	public ResponseEntity<Sala> salaAtual(@PathVariable Long id){
		Usuario buscaUsuario = usuarioService.buscaUsuario(id);
		HistoricoSalaUsuario sala = historicoRepository.findSalaAtual(buscaUsuario.getPerfil().getId());
		Sala ativa = sala.getSala();
		return ResponseEntity.ok().body(ativa);
	}
	
	@GetMapping("/{id}/total")
	@ApiOperation(notes= "Retorna o total gasto em todos os roles do usuario", value= "Total Gasto")
	public BigDecimal totalGeral(@PathVariable Long id) {
		List<HistoricoSalaUsuario> salas = historicoRepository.findByIDPerfil(id);
		BigDecimal total = new BigDecimal(0);
		for(HistoricoSalaUsuario historico : salas ) {
			total = total.add(historico.getTotalParcial());
		}
		return total;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
