package br.com.irole.api.service;

import org.springframework.http.ResponseEntity;

import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;

public interface PedidoService {
	
	/**
	 * Salva pedidos da sala
	 * @param sala um objeto Sala contendo os pedidos a serem persistidos
	 * @return ResponseEntity<Pedido> ou ResponseEntity com uma lista de erros
	 */
	public ResponseEntity<?> salvarPedido(Sala sala);
	
	/**
	 * Altera um pedido
	 * @param idPedido o ID do pedido a ser alterado
	 * @param pedido um objeto contendo os novos valores
	 * @return Pedido
	 */
	public Pedido atualizarPedido(Long idPedido, Pedido pedido);
	
	/**
	 * Apaga um pedido
	 * @param id o ID do pedido a ser apagado
	 * @param pedido um objeto contendo os novos valores
	 * @return ResponseEntity "No Content" ou ResponseEntity com uma lista de erros
	 */
	public ResponseEntity<?> apagarPedido(Long id);
	
}
