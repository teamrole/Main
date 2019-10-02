package br.com.irole.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public Optional<Sala> findByCodigo(String codigo);
	
	@Query(value="SELECT PP.pedido_id FROM pedido_sala AS PS, pedido_perfil AS PP WHERE PP.pedido_id = PS.pedido_id AND PS.sala_id = ?1 AND PP.perfil_id = (SELECT id FROM perfil WHERE id_usuario_fk = ?2)", nativeQuery = true)
	List<Pedido> pedidosSalaPorUsuario(Long id,Long idU);
	
	
	
}
