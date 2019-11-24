package br.com.irole.api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public Optional<Sala> findByCodigoEquals(String codigo);
	
	@Query(value="SELECT P.quantidade, I.valor, P.id AS pedido_id FROM pedido_sala AS PS, pedido_perfil AS PP, pedido AS P, item AS I, perfil AS PF WHERE PF.id = PP.perfil_id AND I.id = P.item_id AND P.id = PS.pedido_id AND PP.pedido_id = PS.pedido_id AND PS.sala_id = ?1 AND PP.perfil_id = (SELECT id FROM perfil WHERE id_usuario_fk = ?2)", nativeQuery = true)
	List<TotalPedido> pedidosSalaPorUsuario(Long id,Long idU);
	
	@Query(value= "SELECT count(perfil_id) from pedido_perfil WHERE pedido_id = ?1", nativeQuery = true)
	Long usuariosPorPedido(Long idP);
	
	@Query(value= "SELECT s.aberta FROM sala AS s, pedido_sala AS ps WHERE ps.pedido_id = ?1 AND ps.sala_id = s.id", nativeQuery = true)
	Boolean salaDoPedidoAberta(Long id);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO pedido_sala (sala_id, pedido_id) values (:idSala, :idPedido)", nativeQuery = true)
	Integer inserePedidosala(@Param("idSala") Long idSala, @Param("idPedido") Long idPedido);
	
	
	public interface TotalPedido {

		 public BigDecimal getValor();

		 public Integer getQuantidade();
		 
		 public Long getPedido_id();

		}
	
}
