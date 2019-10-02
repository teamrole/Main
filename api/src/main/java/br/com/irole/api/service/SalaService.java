package br.com.irole.api.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Pedido;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.SalaRepository;

@Service
public class SalaService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private UsuarioService usuarioService;	
	
	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
			
			
	public void fecharSala(Long id) {
		Sala buscaSala = buscaSala(id);
		buscaSala.setAberta(false);
		salaRepository.save(buscaSala);
		
	}
	
	public void entraSala(Long id, String codigo) {
		if (buscaSalaCodigo(codigo).getAberta()) {
			HistoricoSalaUsuario historicoSalaUsuario = new HistoricoSalaUsuario();
			historicoSalaUsuario.setSala(buscaSalaCodigo(codigo));
			historicoSalaUsuario.setUsuario(usuarioService.buscaUsuario(id));
			historicoRepository.save(historicoSalaUsuario);	
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
	
	public Sala buscaSalaCodigo(String codigo) {
		Optional<Sala> sala = salaRepository.findByCodigo(codigo);		
		if (sala.isPresent()) {
			return sala.get();
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	public BigDecimal fecharParcial(Long id, Long idU) {
		HistoricoSalaUsuario historicoSalaUsuario = historicoRepository.findBySalaUsuario(id, idU);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		historicoSalaUsuario.setData_saida(timestamp);
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPedido = BigDecimal.ZERO;
		List<Pedido> pedidoUsuario = salaRepository.pedidosSalaPorUsuario(id, idU);		
		for (Pedido pedido : pedidoUsuario) {
			totalPedido = pedido.getItem().getValor().multiply(new BigDecimal(pedido.getQuantidade()));
	        total = total.add(totalPedido);
		}
		return total;
		
	}

	public void contaUsuario(Long id, Long idU) {
		/*
		 * Optional<Usuario> usuario = usuarioRepository.findById(idU); Sala buscaSala =
		 * buscaSala(id); List<Pedido> pedido = buscaSala.getPedido();
		 */
	}
	
}
