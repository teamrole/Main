package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.irole.api.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM pedido_perfil WHERE pedido_id = ?1", nativeQuery = true)
	void deletarPP(Long idP);
	
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM pedido_sala WHERE pedido_id = ?1", nativeQuery = true)
	void deletarPS(Long idP);
	
}
