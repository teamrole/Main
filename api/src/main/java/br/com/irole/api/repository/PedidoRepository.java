package br.com.irole.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	@Query(value="SELECT p.perfil from Pedido p where p.id = :id_pedido ORDER BY p.id ASC")
	List<Perfil> listaUsuariosDoPedido(@Param("id_pedido") Long idPedido);
	
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM pedido_perfil WHERE pedido_id = ?1", nativeQuery = true)
	void deletarPP(Long idP);
	
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM pedido_sala WHERE pedido_id = ?1", nativeQuery = true)
	void deletarPS(Long idP);
	
}
