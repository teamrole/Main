package br.com.irole.api.service;

import java.util.ArrayList;
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

import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.Item;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
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
	
	public ResponseEntity<?> salvarPedido(Sala sala) {	
		
		//salaService.buscaSala() tem tratamento para caso uma sala não exista
		Sala s = salaService.buscaSala(sala.getId());
		List<Erro> erros = new ArrayList<>(); 
		
		List<Pedido> pedidoSala = new ArrayList<Pedido>();

		for(Pedido pedido: sala.getPedido()) {
			
			pedido.setPerfil(listaDePerfilSemRepeticao(pedido.getPerfil()));
			
			if(verificaUsuariosEstaNaSala(sala.getId(), pedido.getPerfil())){
				Pedido p = persistePedido(pedido);
				pedidoSala.add(p);							
			}else {
				String nomePedido = (pedido.getItem().getDescricao() != null) ? pedido.getItem().getDescricao() : pedido.getItem().getItemTipo().toString();
				String mensagemUsuario = messageSource.getMessage("recurso.pedido.usuario-invalido", new Object[] {nomePedido},
						LocaleContextHolder.getLocale());
				Erro erro = new Erro(mensagemUsuario, mensagemUsuario);
				erros.add(erro);
			}
		}	
		if(!pedidoSala.isEmpty()) {
			s.setPedido(pedidoSala);
			//TODO se falhar por algum motivo: dar rollback na transação, pois pedido persistido ficará solto
			Sala salaSalva = salaRepository.save(s);
			
			return ResponseEntity.ok(salaSalva.getPedido());
			
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.pedido.nenhum-valido", null, LocaleContextHolder.getLocale());
			Erro erro = new Erro(mensagemUsuario, mensagemUsuario);
			erros.add(erro);
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
		}
				
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
	
	public Pedido atualizar(Long id, Pedido pedido) {
		Optional<Pedido> buscaPedido = pedidoRepository.findById(id);
		if (buscaPedido.isPresent()) {
			BeanUtils.copyProperties(pedido, buscaPedido.get(), "id");
			pedidoRepository.save(buscaPedido.get());
			return buscaPedido.get();			
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	} 
	
	public ResponseEntity<?> apagarPedido(Long id) {
		List<Erro> erros = new ArrayList<>();
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
				Erro erro = new Erro(mensagemUsuario, mensagemUsuario);
				erros.add(erro);
				
			}
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.pedido.nao-encontrado",null, LocaleContextHolder.getLocale());
			Erro erro = new Erro(mensagemUsuario, mensagemUsuario);
			erros.add(erro);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
}
