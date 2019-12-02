package br.com.irole.api.service.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.irole.api.exceptionhandler.AppException;
import br.com.irole.api.model.Item;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.ItemRepository;
import br.com.irole.api.repository.PedidoRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.service.PedidoService;
import br.com.irole.api.service.SalaService;

@Service
public class PedidoServiceImpl implements PedidoService{
	
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
	
	@Override
	public ResponseEntity<?> salvarPedido(Sala sala) {	
		
		//salaService.buscaSala() tem tratamento para caso uma sala não exista
		Sala s = salaService.buscaSala(sala.getId());
		List<AppException> erros = new ArrayList<>(); 
		
		List<Pedido> pedidoSala = new ArrayList<Pedido>();

		for(Pedido pedido: sala.getPedido()) {
			
			pedido.setPerfil(listaDePerfilSemRepeticao(pedido.getPerfil()));
			
			if(verificaUsuariosEstaNaSala(sala.getId(), pedido.getPerfil())){
				Pedido p = persistePedido(pedido);
				pedidoSala.add(p);	
				salaRepository.inserePedidosala(s.getId(), p.getId());
			}else {
				String nomePedido = (pedido.getItem().getDescricao() != null) ? pedido.getItem().getDescricao() : pedido.getItem().getItemTipo().toString();
				String mensagemUsuario = messageSource.getMessage("recurso.pedido.usuario-invalido", new Object[] {nomePedido},
						LocaleContextHolder.getLocale());
				AppException erro = new AppException(mensagemUsuario, mensagemUsuario);
				erros.add(erro);
			}
		}	
		if(!pedidoSala.isEmpty()) {
			
			Sala salaSalva = salaRepository.save(s);
			
			return ResponseEntity.ok(salaSalva.getPedido());
			
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.pedido.nenhum-valido", null, LocaleContextHolder.getLocale());
			AppException erro = new AppException(mensagemUsuario, mensagemUsuario);
			erros.add(erro);
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
		}
				
	}
	
	@Override
	public Pedido atualizarPedido(Long id, Pedido pedidoNovo) {
		Optional<Pedido> buscaPedido = pedidoRepository.findById(id);
		if(!buscaPedido.isPresent())
			throw new EmptyResultDataAccessException(1); 
		
		Pedido pedidoEncontrado = buscaPedido.get();
		
		//Atualiza itens do pedido		
		this.atualizarItemPedido(pedidoEncontrado.getId(), pedidoNovo.getItem());
		
		//Atualiza usuarios do pedido
		if(!isListaUsuarioPedidoIguais(pedidoEncontrado.getId(), pedidoNovo.getPerfil())) {
			pedidoEncontrado.setPerfil(pedidoNovo.getPerfil());
		}
		pedidoEncontrado.setQuantidade(pedidoNovo.getQuantidade());
		Pedido pedidoAlterado = pedidoRepository.save(pedidoEncontrado);
		
		return pedidoAlterado;
	} 

	@Override
	public ResponseEntity<?> apagarPedido(Long id) {
		List<AppException> erros = new ArrayList<>();
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		Boolean sala = salaRepository.salaDoPedidoAberta(id);
		if (pedido.isPresent()) {
			if (sala) {
				pedidoRepository.deletarPP(pedido.get().getId());
				pedidoRepository.deletarPS(pedido.get().getId());
				pedidoRepository.deleteById(pedido.get().getId());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}else {
				String mensagemUsuario = messageSource.getMessage("recurso.sala.fechada",null, LocaleContextHolder.getLocale());
				AppException erro = new AppException(mensagemUsuario, mensagemUsuario);
				erros.add(erro);
				
			}
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.pedido.nao-encontrado",null, LocaleContextHolder.getLocale());
			AppException erro = new AppException(mensagemUsuario, mensagemUsuario);
			erros.add(erro);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
	
	private Boolean isListaUsuarioPedidoIguais(Long idPedido, List<Perfil> usuariosRecebidosNoPut) {
		List<Perfil> listaUsuariosPedido = pedidoRepository.listaUsuariosDoPedido(idPedido);
		if(listaUsuariosPedido.size() != usuariosRecebidosNoPut.size())
			return false;		
		
		//Ordena lista de usuários em ordem crescente de ID
		Collections.sort(usuariosRecebidosNoPut, new Comparator<Perfil>() {
			@Override
			public int compare(Perfil o1, Perfil o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});		
		//Lista de usuários antigos já vem ordenados do banco
		if(!listaUsuariosPedido.equals(usuariosRecebidosNoPut)) 
			return false;
			
		return true;
	}
	
	private void atualizarItemPedido(Long idPedido, Item itemNovo) {
		Optional<Item> buscaItem = itemRepository.findById(idPedido);
		if(!buscaItem.isPresent())
			throw new EmptyResultDataAccessException(1); 
		
		BeanUtils.copyProperties(itemNovo, buscaItem.get());
		itemRepository.save(buscaItem.get());
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
	
	public List<Perfil> listaDePerfilSemRepeticao(List<Perfil> perfis){
		return  perfis.stream() 
		        .distinct()    
		        .collect(Collectors.toList());
	}
		
}
