package br.com.irole.api.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;

@Service
public class RankingService {
	
	//TODO Cachear rankings, principalmente mês, ou usar batchs ao inves recurso
		
	@Autowired
	private HistoricoSalaUsuarioRepository historicoSalaUsuarioRepository;

	public List<HistoricoSalaUsuario> rankingDia() {
		Timestamp hoje = new Timestamp(System.currentTimeMillis());		
		List<HistoricoSalaUsuario> ranking  = historicoSalaUsuarioRepository.rankingDia();
		
		return ranking;
	}


//	public List<Ranking> rankingTodos() {
//		
//		List<Ranking> ranking  = historicoSalaUsuarioRepository.ranking();		
//		
//		return ranking;
//	}
//	
//	public List<Ranking> rankingMes() {
//		Timestamp hoje = new Timestamp(System.currentTimeMillis());
//		String mes = new SimpleDateFormat("MM").format(new Date(hoje.getTime()));
//		String ano = new SimpleDateFormat("yyyy").format(new Date(hoje.getTime()));
//		
//		List<Ranking> ranking = historicoSalaUsuarioRepository.rankingMes(mes, ano);
//		
//		return ranking;
//	}	
	
}
