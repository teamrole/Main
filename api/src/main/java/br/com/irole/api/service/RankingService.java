package br.com.irole.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;

@Service
public class RankingService {
	
	//TODO Cachear rankings, principalmente mês, ou usar batchs ao inves recurso
		
	@Autowired
	private HistoricoSalaUsuarioRepository historicoSalaUsuarioRepository;

	public List<Ranking> rankingDia() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingDia();
		return historicos;
	}

	public List<Ranking> rankingMes() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingMes();		
		return historicos;
	}

	public List<Ranking> rankingTodos() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingTodos();		
		return historicos;
	}
	
}