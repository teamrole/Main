package br.com.irole.api.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.PerfilRepository;
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
	private PerfilRepository perfilRepository;
	
	@Autowired
	private MessageSource messageSource;
				
	public void fecharSala(Long id) {
		Optional<Sala> buscaSala = salaRepository.findById(id);
		List<HistoricoSalaUsuario> usuarios = historicoRepository.findByIDSala(id);
		for (HistoricoSalaUsuario usuario : usuarios) {
			if (usuario.getData_saida() == null) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				usuario.setData_saida(timestamp);
				usuario.setAtivo(false);
			}
		}
		buscaSala.get().setAberta(false);
		salaRepository.save(buscaSala.get());
		
	}
	
	public List<HistoricoSalaUsuario> usuariosSala(Long id){
		List<HistoricoSalaUsuario> historico = historicoRepository.findByIDSala(id);
		List<HistoricoSalaUsuario> usuarios = new ArrayList<HistoricoSalaUsuario>();
		for (HistoricoSalaUsuario usuario : historico) {
			if (usuario.getData_saida() == null) {
				usuario.setAtivo(true);
				usuarios.add(usuario);
			}else {
				usuario.setAtivo(false);
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}
	
	public ResponseEntity<?> entraSala(Long id, String codigo) {

		Optional<Sala> sala = buscaSalaCodigo(codigo);	
		Usuario buscaUsuario = usuarioService.buscaUsuario(id);
		HistoricoSalaUsuario salaAtual = historicoRepository.findSalaAtual(buscaUsuario.getPerfil().getId());		
		if(!sala.isPresent()) 
			throw new EmptyResultDataAccessException(1);	
		if (sala.get().getAberta() && salaAtual == null) {
				HistoricoSalaUsuario historicoSalaUsuario = new HistoricoSalaUsuario();									
				historicoSalaUsuario.setSala(sala.get());
				historicoSalaUsuario.setPerfil(buscaUsuario.getPerfil());
				historicoSalaUsuario.setAtivo(true);
				historicoRepository.save(historicoSalaUsuario);	
				return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalaUsuario);			
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario-na-sala", null, LocaleContextHolder.getLocale());
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);			
		}
		
		
	}
	
	public Sala buscaSala(Long id) {
		Optional<Sala> sala = salaRepository.findById(id);		
		if (sala.isPresent()) {
			return sala.get();
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	public Optional<Sala> buscaSalaCodigo(String codigo) {
		Optional<Sala> sala = salaRepository.findByCodigoEquals(codigo);	
		return sala;
	}
	
	public BigDecimal fecharParcial(Long id, Long idU) {
		
		Usuario usuario = usuarioService.buscaUsuario(idU);
		
		HistoricoSalaUsuario historicoSalaUsuario = historicoRepository.findBySalaUsuario(id, usuario.getPerfil().getId());
		
		if(historicoSalaUsuario == null)
			throw new EmptyResultDataAccessException(1);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		historicoSalaUsuario.setData_saida(timestamp);
		historicoSalaUsuario.setTotalParcial(contaParcial(id, idU));
		historicoSalaUsuario.setAtivo(false);
		historicoRepository.save(historicoSalaUsuario);
		return contaParcial(id, idU);
		
	}
	
	
	//TODO trocar para receber pefil id no parametro
	public BigDecimal contaParcial(Long id, Long idU) {
		Usuario usuario = usuarioService.buscaUsuario(idU);
		
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPedido = BigDecimal.ZERO;		
		List<TotalPedido> pedidoUsuario = salaRepository.pedidosSalaPorUsuario(id, usuario.getPerfil().getId());	
		
		if(pedidoUsuario.isEmpty())
			return new BigDecimal(0);
		
		Long usuarioPorPedido;
		for (TotalPedido pedido : pedidoUsuario) {
			totalPedido = pedido.getValor().multiply(new BigDecimal(pedido.getQuantidade()));
			//TODO PEgar somente os usuarios ativo
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
			
			total = total.add(sala.getTotalParcial());
		}		
		return total;
	}
	
	public Boolean isUsuarioNaSala(Long id_sala, Perfil perfil) {
		
		Optional<Perfil> p = perfilRepository.findById(perfil.getId());
		
		if(!p.isPresent())     
			return false;
		
		HistoricoSalaUsuario salaUsuario = historicoRepository.findBySalaUsuario(id_sala, p.get().getUsuario().getId());
		
		if(salaUsuario == null || salaUsuario.getData_saida() != null)
				return false;
		
		return true;
	}
	
}
