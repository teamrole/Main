package br.com.irole.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Item;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.ItemRepository;
import br.com.irole.api.repository.PedidoRepository;
import br.com.irole.api.repository.SalaRepository;

@Service
public class PedidoService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Pedido> salvarPedido(Sala sala) {
		//TODO: Spring Transactional
		
		for(Pedido pedido: sala.getPedido()) {
			Item itemSalvo = itemRepository.save(pedido.getItem());
			pedido.setItem(itemSalvo);
			pedidoRepository.save(pedido);		
		}	
		
		Sala salaSalva = salaRepository.save(sala);
	
		return (salaSalva != null) ? salaSalva.getPedido() : null;
	}
}
