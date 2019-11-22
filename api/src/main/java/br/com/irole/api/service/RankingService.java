package br.com.irole.api.service;

import java.util.List;

import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;

public interface RankingService {
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos do dia 
	 * @return List<Ranking> 
	 */
	public List<Ranking> rankingDia();
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos do mês 
	 * @return List<Ranking> 
	 */
	public List<Ranking> rankingMes(); 
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos de todo tempo de uso da aplicação
	 * @return List<Ranking> 
	 */
	public List<Ranking> rankingTodos();
}
