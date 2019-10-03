package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.irole.api.model.HistoricoSalaUsuario;

public interface HistoricoSalaUsuarioRepository extends JpaRepository<HistoricoSalaUsuario, Long>{

	@Query(value = "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND usuario_id = ?2", nativeQuery = true)
	  HistoricoSalaUsuario findBySalaUsuario(Long id, Long idU);
}
