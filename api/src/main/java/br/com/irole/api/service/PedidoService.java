package br.com.irole.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.Item;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;
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
	private SalaService salaService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	public List<Pedido> salvarPedido(Sala sala) {	
		
		//buscaSala() tem tratamento para caso uma sala não exista
		Sala s = salaService.buscaSala(sala.getId());
		List<Erro> erros = new ArrayList<>(); 
		
		List<Pedido> pedidoSala = new ArrayList<Pedido>();

		for(Pedido pedido: s.getPedido()) {
			
			if(verificaUsuariosEstaNaSala(s.getId(), pedido.getPerfil())){
				Pedido p = persistePedido(pedido);
				pedidoSala.add(p);							
			}
		}	
		if(!pedidoSala.isEmpty()) {
			s.setPedido(pedidoSala);
			Sala salaSalva = salaRepository.save(s);
			
			return salaSalva.getPedido();
			
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.pedido.nenhum-valido", null, LocaleContextHolder.getLocale());
			Erro erro = new Erro(mensagemUsuario, mensagemUsuario);
			erros.add(erro);
		}
		
		return  null;
		
	}
	
	public Pedido persistePedido(Pedido pedido) {
		
		Item itemSalvo = itemRepository.save(pedido.getItem());
		pedido.setItem(itemSalvo);
		Pedido pedidoSave = pedidoRepository.save(pedido);		
		return pedidoSave;
	}
	
	public Boolean verificaUsuariosEstaNaSala(Long sala_id, List<Perfil> perfil) {

		for (Perfil p : perfil) {
			if(!salaService.isUsuarioNaSala(sala_id, p))
				return false;
		}
		return true;
	}
}
