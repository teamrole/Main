package br.com.irole.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		Optional<Sala> s = salaRepository.findById(sala.getId());
		List<Pedido> pedidoSala = new ArrayList<Pedido>();

		for(Pedido pedido: sala.getPedido()) {
			Item itemSalvo = itemRepository.save(pedido.getItem());
			pedido.setItem(itemSalvo);
			pedidoRepository.save(pedido);		
			pedidoSala = s.get().getPedido();
			pedidoSala.add(pedido);
		}	
		s.get().setPedido(pedidoSala);
		Sala salaSalva = salaRepository.save(s.get());
	
		return (salaSalva != null) ? salaSalva.getPedido() : null;
	}
}
