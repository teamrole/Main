package br.com.irole.api.service;

import java.util.Date;
import java.util.List;

import br.com.irole.api.repository.HistoricoSalaUsuarioRepository.Ranking;

public interface RankingService {
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos do dia especificado
	 * @param date uma data no padrão americano
	 * @return List of Ranking
	 */
	public List<Ranking> rankingDia(Date date);
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos do mês (na data especificada)
	 * @param date uma data no padrão americano
	 * @return List of Ranking
	 */
	public List<Ranking> rankingMes(Date date); 
	
	/**
	 * Monta um ranking onde mostra os usuários mais ativos (de todo tempo de uso da aplicação)
	 * @return List of Ranking
	 */
	public List<Ranking> rankingTodos();
}
