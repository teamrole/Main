package br.com.irole.api.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Ranking;
import br.com.irole.api.model.TipoRankingEnum;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;

@Service
public class RankingService {
	
	//TODO Cachear rankings, principalmente mês, ou usar batchs ao inves recurso
		
	@Autowired
	private HistoricoSalaUsuarioRepository historicoSalaUsuarioRepository;

	public List<Perfil> rankingDia() {
		List<HashMap<Long, Perfil>> historicos  = historicoSalaUsuarioRepository.rankingDia();
		
		for (HashMap<Long, Perfil> h: historicos) {
			System.out.println(h.toString());
		}
		//List<Ranking> ranking = converteParaRanking(historicos, TipoRankingEnum.DIA);
		
		return null;
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


	private Ranking converteParaRanking(List<HistoricoSalaUsuario> historicos, TipoRankingEnum tipoRanking) {
		
//		List<Ranking> rankins = new ArrayList<>();
//		
//		historicos.stream().map( h -> {
//			Ranking ranking = new Ranking();
//			Long quantidade = historicoSalaUsuarioRepository.countRoleUsuario(historicosID)
//			
//			ranking
//			return ranking;
//		});
//		
//		
//		Ranking ranking = new Ranking();
//		ranking.setTotalRoles(historicosDoUsuario.size());
//		ranking.setPerfil(historicosDoUsuario.get(0).getPerfil());
//		ranking.setTipoRanking(tipoRanking);
//		
//		return ranking;
		return null;
	}

}