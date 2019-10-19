 package br.com.irole.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.irole.api.model.HistoricoSalaUsuario;

public interface HistoricoSalaUsuarioRepository extends JpaRepository<HistoricoSalaUsuario, Long>{

	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	  HistoricoSalaUsuario findBySalaUsuario(Long id, Long idU);
	 
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE usuario_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDUsuario(Long id);
	  
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	  List<HistoricoSalaUsuario> findByIDSala(Long id);
	  
	  @Query(value = "SELECT count(usuario_id) FROM historico_sala_usuario WHERE sala_id = ?1", nativeQuery = true)
	  Long findRolezeros(Long id);
	
}
