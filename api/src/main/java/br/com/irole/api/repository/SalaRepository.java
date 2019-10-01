package br.com.irole.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public Optional<Sala> findByCodigo(String codigo);
	
	@Query(value="SELECT SUM(sala.pedido.item.valor) * sala.pedido.quantidade) / COUNT(sala.pedido.perfil) FROM sala WHERE sala.id = :id AND sala.pedido.perfil.usuario.id = :idU", nativeQuery=true)
	public float fecharContaUsuario(@Param("id") Long id, @Param("idU") Long idU);
	
}
