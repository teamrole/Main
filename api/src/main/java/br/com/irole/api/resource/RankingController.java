package br.com.irole.api.resource;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;

@RestController("/rankings")
public class RankingController {
	
	@Autowired
	HistoricoSalaUsuarioRepository historicoRepository;
	
	@RequestMapping("/usuarios/{id}/query")
	public ResponseEntity<List<HistoricoSalaUsuario>> getRanking(@PathVariable Long user_id, @PathVariable String query) {
		
		String filter_query = "";		
		Timestamp hoje = new Timestamp(System.currentTimeMillis());
		
		switch (query) {
		case "dia":
			filter_query = "and h.data_entrada <= "+ hoje +" and "+hoje+" <= h.data_saida" ; 
			break;
			
		case "mes":
			
			break;		

		default:
			//todos os roles
			
			break;
		}
		
		List<HistoricoSalaUsuario> historico = historicoRepository.buscaComFiltro(user_id, filter_query);
		
		return ResponseEntity.ok(historico);
	}

}
