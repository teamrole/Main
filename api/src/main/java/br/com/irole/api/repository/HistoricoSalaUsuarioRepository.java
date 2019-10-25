 package br.com.irole.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.HistoricoSalaUsuario;

public interface HistoricoSalaUsuarioRepository extends JpaRepository<HistoricoSalaUsuario, Long>{

	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND perfil_id = ?2", nativeQuery = true)
	  HistoricoSalaUsuario findBySalaUsuario(Long id, Long idU);
	 
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE perfil_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDUsuario(Long id);
	  
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDSala(Long id);
	  
	@Query(value = "SELECT count(perfil_id) FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	Long findRolezeros(Long id);	 
	
	@Query(value = "select count(id) as contador, perfil_id as usuario from historico_sala_usuario as h where data_hora_saida is not null and DATE(h.data_hora_entrada) = :date GROUP BY usuario_id order by count(id) DESC", nativeQuery=true)
	List<Ranking> rankingDia(@Param("date") String date);
	
	@Query(value = "select count(id) as contador, perfil_id as usuario from historico_sala_usuario as h where data_hora_saida is not null and MONTH(h.data_hora_entrada) = :mes and YEAR(h.data_hora_entrada) = :ano GROUP BY usuario_id order by count(id) DESC", nativeQuery=true)
	List<Ranking> rankingMes(@Param("mes") String mes, @Param("ano") String ano);

	@Query(value = "select count(id) as contador, perfil_id as usuario from historico_sala_usuario as h where data_hora_saida is not null GROUP BY usuario_id order by count(id) DESC", nativeQuery=true)
	List<Ranking> ranking();

	public interface Ranking{
		public Long getContador();
		public Long getUsuario();		
	}

}
