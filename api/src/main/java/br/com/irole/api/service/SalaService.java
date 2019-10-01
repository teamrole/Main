package br.com.irole.api.service;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.repository.UsuarioRepository;

@Service
public class SalaService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
			
			
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
		}else {
			
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

	public Sala fecharContaUsuario(Long id, Long idU) {
		float total =  salaRepository.fecharContaUsuario(id, idU);
		System.out.println(total);
		/*
		 * Optional<Usuario> usuario = usuarioRepository.findById(idU); Sala buscaSala =
		 * buscaSala(id); List<Pedido> pedido = buscaSala.getPedido();
		 */
		return null;
	}
	
}
