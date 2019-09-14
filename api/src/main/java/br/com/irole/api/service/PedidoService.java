package br.com.irole.api.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.PedidoRepository;
import br.com.irole.api.repository.SalaRepository;

@Service
public class PedidoService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Pedido salvarPedido(Sala sala, Pedido pedido) {
		//TODO: Spring Transactional: ATOMIC
	
		Sala salaSalva = null;		
		Pedido pedidoSalvo = pedidoRepository.save(pedido);		
		
		if(pedidoSalvo!=null) {
		
			sala.setPedido(Arrays.asList(pedidoSalvo));
			salaSalva = salaRepository.save(sala);
		}
	
		return (salaSalva != null) ? salaSalva.getPedido().get(0) : null;
	}
}
