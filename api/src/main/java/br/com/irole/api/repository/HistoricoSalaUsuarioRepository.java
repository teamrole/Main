 package br.com.irole.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
	
	@Query(value = "SELECT * FROM historico_sala_usuario WHERE perfil_id = ?1 AND data_hora_saida IS NULL", nativeQuery = true)
	  HistoricoSalaUsuario findSalaAtual(Long id);
	
	@Query(value= "SELECT * FROM historico_sala_usuario WHERE sala_id = ?1 AND data_hora_saida IS NULL)", nativeQuery = true)
	  List<HistoricoSalaUsuario> findUsuariosAtivo(Long id);
	
	@Query(value = "select count(h.id) as contador, p.id as perfil_id, p.foto as foto, p.nome as nome, p.id_usuario_fk as usuario from historico_sala_usuario h left join perfil p on h.perfil_id = p.id where DATE(h.data_hora_entrada) = CURDATE() and h.data_hora_saida is not null GROUP BY h.perfil_id order by count(h.id) DESC", nativeQuery = true)
	List<Ranking> rankingDia();
	
	@Query(value = "select count(h.id) as contador, p.id as perfil_id, p.foto as foto, p.nome as nome, p.id_usuario_fk as usuario from historico_sala_usuario h left join perfil p on h.perfil_id = p.id  where h.data_hora_saida is not null and MONTH(h.data_hora_entrada) = MONTH(CURDATE()) and YEAR(h.data_hora_entrada) = YEAR(CURDATE()) GROUP BY h.perfil_id order by count(h.id) DESC", nativeQuery=true)
	List<Ranking> rankingMes();

	@Query(value = "select count(h.id) as contador, p.id as perfil_id, p.foto as foto, p.nome as nome, p.id_usuario_fk as usuario from historico_sala_usuario h left join perfil p on h.perfil_id = p.id where h.data_hora_saida is not null GROUP BY h.perfil_id order by count(h.id) DESC", nativeQuery = true)
	List<Ranking> rankingTodos();
	
	public interface Ranking{
		Long getContador();
		Long getPerfil_id();
		String getFoto();
		String getNome();
		Long getUsuario();
	}

	

}
