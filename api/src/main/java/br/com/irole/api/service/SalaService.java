package br.com.irole.api.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Item;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.repository.SalaRepository.TotalPedido;

@Service
public class SalaService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private UsuarioService usuarioService;	
	
	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private MessageSource messageSource;
			
			
	public void fecharSala(Long id) {
		Optional<Sala> buscaSala = salaRepository.findById(id);
		List<HistoricoSalaUsuario> usuarios = historicoRepository.findByIDSala(id);
		for (HistoricoSalaUsuario usuario : usuarios) {
			if (usuario.getData_saida() == null) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				usuario.setData_saida(timestamp);
			}
		}
		buscaSala.get().setAberta(false);
		salaRepository.save(buscaSala.get());
		
	}
	
	public ResponseEntity<?> entraSala(Long id, String codigo) {

		Optional<Sala> sala = buscaSalaCodigo(codigo);	
		HistoricoSalaUsuario salaAtiva = historicoRepository.findSalaAtiva(id);
		if(!sala.isPresent()) 
			throw new EmptyResultDataAccessException(1);	
		
		if (sala.get().getAberta() && salaAtiva == null) {
				HistoricoSalaUsuario historicoSalaUsuario = new HistoricoSalaUsuario();									
				historicoSalaUsuario.setSala(sala.get());
				historicoSalaUsuario.setUsuario(usuarioService.buscaUsuario(id));
				historicoRepository.save(historicoSalaUsuario);	
				return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalaUsuario);			
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario-na-sala", null, LocaleContextHolder.getLocale());
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);			
		}
		
		
	}
	
	public Sala buscaSala(Long id) {
		Optional<Sala> salaSalvo = salaRepository.findById(id);		
		if (salaSalvo.isPresent()) {
			return salaSalvo.get();
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	public Optional<Sala> buscaSalaCodigo(String codigo) {
		Optional<Sala> sala = salaRepository.findByCodigoEquals(codigo);	
		return sala;
	}
	
	public BigDecimal fecharParcial(Long id, Long idU) {
		HistoricoSalaUsuario historicoSalaUsuario = historicoRepository.findBySalaUsuario(id, idU);
		if(historicoSalaUsuario == null)
			throw new EmptyResultDataAccessException(1);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		historicoSalaUsuario.setData_saida(timestamp);
		historicoSalaUsuario.setTotalParcial(contaParcial(id, idU));
		historicoRepository.save(historicoSalaUsuario);
		return contaParcial(id, idU);
		
	}

	public BigDecimal contaParcial(Long id, Long idU) {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPedido = BigDecimal.ZERO;		
		List<TotalPedido> pedidoUsuario = salaRepository.pedidosSalaPorUsuario(id, idU);	
		
		if(pedidoUsuario.isEmpty())
			return new BigDecimal(0);
		
		Long usuarioPorPedido;
		for (TotalPedido pedido : pedidoUsuario) {
			totalPedido = pedido.getValor().multiply(new BigDecimal(pedido.getQuantidade()));
			usuarioPorPedido = salaRepository.usuariosPorPedido(pedido.getPedido_id());
			totalPedido = totalPedido.divide(new BigDecimal(usuarioPorPedido));
	        total = total.add(totalPedido);
		}
		return total;
		/*
		 * Optional<Usuario> usuario = usuarioRepository.findById(idU); Sala buscaSala =
		 * buscaSala(id); List<Pedido> pedido = buscaSala.getPedido();
		 */
	}
	
	public BigDecimal totalSala(Long id) {
		BigDecimal total = new BigDecimal(0);
		List<HistoricoSalaUsuario> salas = historicoRepository.findByIDSala(id);
		for(HistoricoSalaUsuario sala : salas) {
			
			total.add(sala.getTotalParcial());
		}		
		return total;
	}
	
}
