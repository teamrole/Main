package br.com.irole.api.resource;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.exceptionhandler.AppException;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;
import br.com.irole.api.service.RankingService;
import br.com.irole.api.service.util.ConversorData;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("rankings")
public class RankingController {
	
	@Autowired
	private RankingService rankingService;
	
	@Autowired
	private MessageSource messageSource;
		
	@GetMapping
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês", value = "Ranking de rolês gerais")
	public ResponseEntity<List<Ranking>> getRanking() {
		
		List<Ranking> ranking  = rankingService.rankingTodos();
		
		return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/dia")
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês na data especificada."
			+ "Como parâmetro se espera o 'date' recebendo uma data no padrão americano", value = "Ranking do dia")
	public ResponseEntity<?> getRankingDia(@RequestParam String date) {
		
		Date dataConvertida = ConversorData.stringToDate(date);
		
		if(dataConvertida != null) {
			List<Ranking> ranking = rankingService.rankingDia(dataConvertida);
			return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
			
		}else {			
			String message = messageSource.getMessage("recurso.ranking.data-invalida", new Object[] {date},
			LocaleContextHolder.getLocale());
			AppException erro = new AppException(message, message);
			return ResponseEntity.badRequest().body(erro);
		}
	}
	
	@GetMapping("/mes")
	@ApiOperation(notes = "Retorna uma lista de pessoas que mais participaram de rolês no mês da data especificada. "
			+ "Como parâmetro se espera o 'date' recebendo uma data no padrão americano",
	value = "Ranking do mês")
	public ResponseEntity<?> getRankingMes(@RequestParam String date) {
		
		Date dataConvertida = ConversorData.stringToDate(date);
		
		if(dataConvertida != null) {
			List<Ranking> ranking = rankingService.rankingMes(dataConvertida);
			return (!ranking.isEmpty()) ? ResponseEntity.ok(ranking) : ResponseEntity.noContent().build();
			
		}else {
			String message = messageSource.getMessage("recurso.ranking.data-invalida", new Object[] {date},
			LocaleContextHolder.getLocale());
			AppException erro = new AppException(message, message);
			return ResponseEntity.badRequest().body(erro);
		}
	}

}
