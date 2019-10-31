 package br.com.irole.api.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;

public interface HistoricoSalaUsuarioRepository extends JpaRepository<HistoricoSalaUsuario, Long>{

	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND perfil_id = ?2", nativeQuery = true)
	  HistoricoSalaUsuario findBySalaUsuario(Long id, Long idU);
	 
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE perfil_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDUsuario(Long id);
	  
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDSala(Long id);
	  
	@Query(value = "SELECT count(perfil_id) FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	Long findRolezeros(Long id);	 
	
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE perfil_id = ?1 AND data_hora_saida IS NULL", nativeQuery = true)
	  HistoricoSalaUsuario findSalaAtual(Long id);
	
	@Query(value= "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND data_hora_saida IS NULL)", nativeQuery = true)
	  List<HistoricoSalaUsuario> findUsuariosAtivo(Long id);

	@Query(value = "select count(h) as totalRoles, h.perfil as perfil from HistoricoSalaUsuario h where h.data_saida is not null and h.data_entrada = :date GROUP BY h.perfil order by count(h) DESC")
	List<HistoricoSalaUsuario> rankingDia();
	/*
	@Query(value = "select count(id) as contador, perfil_id as usuario from HistoricoSalaUsuario as h where data_hora_saida is not null and MONTH(h.data_hora_entrada) = :mes and YEAR(h.data_hora_entrada) = :ano GROUP BY usuario_id order by count(id) DESC", nativeQuery=true)
	List<HistoricoSalaUsuario> rankingMes(@Param("mes") String mes, @Param("ano") String ano);

	@Query(value = "select count(id) as contador, perfil_id as usuario from HistoricoSalaUsuario as h where data_hora_saida is not null GROUP BY usuario_id order by count(id) DESC", nativeQuery=true)
	List<HistoricoSalaUsuario> ranking();
	*/

}
