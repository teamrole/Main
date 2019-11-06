package br.com.irole.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;
import br.com.irole.api.service.RankingService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("rankings")
public class RankingController {
	
	@Autowired
	private RankingService rankingService;
	
	@GetMapping
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês", value = "Capturar ranking sem filtro")
	public ResponseEntity<List<Ranking>> getRanking() {
		
		List<Ranking> ranking  = rankingService.rankingTodos();
		
		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/dia")
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês hoje; *Somente as pessoas que já saíram da sala", value = "Capturar ranking do dia")
	public ResponseEntity<List<Ranking>> getRankingDia() {
		
		List<Ranking> ranking  = rankingService.rankingDia();
		
		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/mes")
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês no mês atual", value = "Capturar ranking do mês")
	public ResponseEntity<List<Ranking>> getRankingMes() {
		
		List<Ranking> ranking = rankingService.rankingMes();
		
		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
	}

}
