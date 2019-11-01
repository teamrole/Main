package br.com.irole.api.model;

public class Ranking {
	
	private long totalRoles;
	private Perfil perfil;
	private TipoRankingEnum tipoRanking;
	
	public long getTotalRoles() {
		return totalRoles;
	}
	public void setTotalRoles(long totalRoles) {
		this.totalRoles = totalRoles;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public TipoRankingEnum getTipoRanking() {
		return tipoRanking;
	}
	public void setTipoRanking(TipoRankingEnum tipoRanking) {
		this.tipoRanking = tipoRanking;
	}
	
	
}
