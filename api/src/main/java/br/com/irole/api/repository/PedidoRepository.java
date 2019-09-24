package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.irole.api.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

	
}
