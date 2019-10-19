package br.com.irole.api.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.service.SalaService;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {
	

	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping("/usuarios/{id}")
	public ResponseEntity<List<HistoricoSalaUsuario>> getHistorico(@PathVariable Long id){
		List<HistoricoSalaUsuario> salas = historicoRepository.findByIDUsuario(id);
		List<HistoricoSalaUsuario> historicos = new ArrayList<HistoricoSalaUsuario>();;
		for(HistoricoSalaUsuario historico : salas ) {
			historico.setTotalSala(salaService.totalSala(historico.getSala().getId()));
			historico.setTotalUsuarios(historicoRepository.findRolezeros(historico.getSala().getId()));
			historicos.add(historico);
		}
		
		return !historicos.isEmpty() ? ResponseEntity.ok(historicos) : ResponseEntity.noContent().build();
		
		
		
	}

}