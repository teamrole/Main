package br.com.irole.api.service;

import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.repository.UsuarioRepository;

@Service
public class SalaService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
			
			
	public void fecharSala(Long id) {
		Sala buscaSala = buscaSala(id);
		buscaSala.setAberta(false);
		salaRepository.save(buscaSala);
		
	}
	
	public void entraSala(Long id, String codigo) {
		String sql = "INSERT INTO historico_sala_usuario(sala_id, perfil_id, data_hora_entrada, data_hora_saida) "
				+ "SELECT sala.id, perfil.id, GETDATE(), null FROM sala, perfil WHERE sala.codigo = :codigo AND  perfil.id_usuario_fk = :id";
		Query query = entityManagerFactory.createEntityManager().createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("codigo", codigo);
		int result = query.executeUpdate();
		
		
	}
	
	public Sala buscaSala(Long id) {
		Optional<Sala> salaSalvo = salaRepository.findById(id);		
		if (salaSalvo.isPresent()) {
			return salaSalvo.get();
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}

	public Sala fecharContaUsuario(Long id, Long idU) {
		String sql = "SELECT SUM(sala.pedido.item.valor) * sala.pedido.quantidade) / COUNT(sala.pedido.perfil) "
				+ "FROM sala "
				+ "WHERE sala.id = :id AND sala.pedido.perfil.usuario.id = :idU";
		Query query = entityManagerFactory.createEntityManager().createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("idU", idU);
		BigDecimal total = (BigDecimal) query.getSingleResult();
		System.out.println(total);
		/*
		 * Optional<Usuario> usuario = usuarioRepository.findById(idU); Sala buscaSala =
		 * buscaSala(id); List<Pedido> pedido = buscaSala.getPedido();
		 */
		return null;
	}
	
}
