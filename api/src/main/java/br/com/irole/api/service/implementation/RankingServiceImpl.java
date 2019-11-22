package br.com.irole.api.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;
import br.com.irole.api.service.RankingService;

@Service
public class RankingServiceImpl implements RankingService{
	
	//TODO Cachear rankings, principalmente mês, ou usar batchs ao inves recurso
		
	@Autowired
	private HistoricoSalaUsuarioRepository historicoSalaUsuarioRepository;

	@Override
	public List<Ranking> rankingDia() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingDia();
		return historicos;
	}

	@Override
	public List<Ranking> rankingMes() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingMes();		
		return historicos;
	}

	@Override
	public List<Ranking> rankingTodos() {
		List<Ranking> historicos  = historicoSalaUsuarioRepository.rankingTodos();		
		return historicos;
	}
	
}