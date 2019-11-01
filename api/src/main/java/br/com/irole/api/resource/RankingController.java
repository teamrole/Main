package br.com.irole.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.model.Perfil;
import br.com.irole.api.service.RankingService;

@RestController
@RequestMapping("rankings")
public class RankingController {
	
	@Autowired
	private RankingService rankingService;
	
//	@GetMapping
//	public ResponseEntity<List<HistoricoSalaUsuario>> getRanking() {
//		
//		List<HistoricoSalaUsuario> ranking  = rankingService.rankingTodos();
//		
//		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
//	}
	
	@GetMapping("/dia")
	public ResponseEntity<List<Perfil>> getRankingDia() {
		
		//Pega o rankings de pessoas que participaram em mais roles hoje, contabiliza somente as salas já fechadas		
		List<Perfil> ranking  = rankingService.rankingDia();
		
		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
	}
//	
//	@GetMapping("/mes")
//	public ResponseEntity<List<Ranking>> getRankingMes() {
//		
//		List<Ranking> ranking = rankingService.rankingMes();
//		
//		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
//	}

}
