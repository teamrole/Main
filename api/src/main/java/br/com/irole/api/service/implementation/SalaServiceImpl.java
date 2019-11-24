package br.com.irole.api.service.implementation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
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
import br.com.irole.api.service.SalaService;

@Service
public class SalaServiceImpl implements SalaService{

	@Autowired
	private SalaRepository salaRepository;

	@Autowired
	private UsuarioServiceImpl usuarioService;

	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private MessageSource messageSource;

	@Override
	public void fecharSala(Long id) {
		Optional<Sala> buscaSala = salaRepository.findById(id);
		List<HistoricoSalaUsuario> historicoUsuariosDaSala = historicoRepository.findByIDSala(id);
	
		for (HistoricoSalaUsuario historicoUsuario : historicoUsuariosDaSala) {
			//TODO controle transactional se remover um usuario falhar	
			if (historicoUsuario.getData_saida() == null) {
				fecharContaDoUsuario(id, historicoUsuario.getPerfil().getUsuario().getId());
			}
		}
		buscaSala.get().setAberta(false);
		salaRepository.save(buscaSala.get());

	}

	@Override
	public List<HistoricoSalaUsuario> usuariosSala(Long id) {
		List<HistoricoSalaUsuario> historico = historicoRepository.findByIDSala(id);
		List<HistoricoSalaUsuario> usuarios = new ArrayList<HistoricoSalaUsuario>();
		for (HistoricoSalaUsuario usuario : historico) {
			if (usuario.getData_saida() == null) {
				usuario.setAtivo(true);
				usuarios.add(usuario);
			} else {
				usuario.setAtivo(false);
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}

	@Override
	public ResponseEntity<?> entraSala(Long id, String codigo) {

		Optional<Sala> sala = buscaSalaPeloCodigo(codigo);
		if (!sala.isPresent())
			throw new EmptyResultDataAccessException(1);
		
		Usuario buscaUsuario = usuarioService.buscaUsuario(id);
		HistoricoSalaUsuario salaAtual = historicoRepository.findSalaAtual(buscaUsuario.getPerfil().getId());
		HistoricoSalaUsuario historico = historicoRepository.findBySalaUsuario(sala.get().getId(), buscaUsuario.getId());
		
		if (historico == null) {
			if (sala.get().getAberta() && salaAtual == null) {
				HistoricoSalaUsuario historicoSalaUsuario = new HistoricoSalaUsuario();
				historicoSalaUsuario.setSala(sala.get());
				historicoSalaUsuario.setPerfil(buscaUsuario.getPerfil());
				historicoSalaUsuario.setAtivo(true);
				historicoRepository.save(historicoSalaUsuario);
				return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalaUsuario);
			} else {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario-na-sala", null,
						LocaleContextHolder.getLocale());
				List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
			}
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario.historico", null,
					LocaleContextHolder.getLocale());
			List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
		}
		

	}

	@Override
	public Sala buscaSala(Long id) {
		Optional<Sala> sala = salaRepository.findById(id);
		if (sala.isPresent()) {
			return sala.get();
		} else {
			throw new EmptyResultDataAccessException(1);
		}
	}

	@Override
	public Optional<Sala> buscaSalaPeloCodigo(String codigo) {
		Optional<Sala> sala = salaRepository.findByCodigoEquals(codigo);
		return sala;
	}

	@Override
	public BigDecimal fecharContaDoUsuario(Long id, Long idU) {

		Usuario usuario = usuarioService.buscaUsuario(idU);

		HistoricoSalaUsuario historicoSalaUsuario = historicoRepository.findBySalaUsuario(id,
				usuario.getPerfil().getId());

		if (historicoSalaUsuario == null) {
			
			throw new EmptyResultDataAccessException(1);
		} else {
			
			BigDecimal pegaContaDeUmUsuario = pegaContaDeUmUsuario(id, idU);
			
			if (historicoSalaUsuario.getData_saida() == null) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				historicoSalaUsuario.setData_saida(timestamp);
				historicoSalaUsuario.setTotalParcial(pegaContaDeUmUsuario);
				historicoSalaUsuario.setAtivo(false);
				historicoRepository.save(historicoSalaUsuario);
				return pegaContaDeUmUsuario;
				
			}else {
				throw new EmptyResultDataAccessException("O usuario ja saiu da sala!", 1);
			}
		}

	}

	// TODO trocar para receber pefil id no parametro
	@Override
	public BigDecimal pegaContaDeUmUsuario(Long id, Long idU) {

		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPedido = BigDecimal.ZERO;
		List<TotalPedido> pedidoUsuario = salaRepository.pedidosSalaPorUsuario(id, idU);

		if (pedidoUsuario.isEmpty())
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
		for (HistoricoSalaUsuario sala : salas) {

			total = total.add(sala.getTotalParcial());
		}
		return total;
	}

	public Boolean isUsuarioNaSala(Long id_sala, Perfil perfil) {

		Optional<Perfil> p = perfilRepository.findById(perfil.getId());

		if (!p.isPresent())
			return false;

		HistoricoSalaUsuario salaUsuario = historicoRepository.findBySalaUsuario(id_sala, p.get().getId());

		if (salaUsuario == null || salaUsuario.getData_saida() != null)
			return false;

		return true;
	}

	@Override
	public Sala criarSala() {
		Sala novaSala = new Sala();
		novaSala.setCodigo(RandomStringUtils.randomAlphanumeric(4));
		salaRepository.save(novaSala);
		
		return novaSala;
	}

	@Override
	public ResponseEntity<?> editarNomeSala(Long id, String nomeDaSala) {
		nomeDaSala = nomeDaSala.replaceAll("\"", "");
		Sala buscaSala = this.buscaSala(id);
		if(buscaSala.getAberta()) {
			buscaSala.setNome(nomeDaSala);
			Sala salaSalva = salaRepository.save(buscaSala);
			return ResponseEntity.ok(salaSalva);
		}
			return ResponseEntity.badRequest().body("Só é possível editar salas abertas");
	}

}
