package br.com.irole.api.service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
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

import br.com.irole.api.exceptionhandler.AppException;
import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Sala;
import br.com.irole.api.repository.HistoricoSalaUsuarioRepository;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.repository.SalaRepository;
import br.com.irole.api.repository.SalaRepository.TotalPedido;
import br.com.irole.api.service.PerfilService;
import br.com.irole.api.service.SalaService;

@Service
public class SalaServiceImpl implements SalaService{

	@Autowired
	private SalaRepository salaRepository;

	@Autowired
	private HistoricoSalaUsuarioRepository historicoRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PerfilService perfilService;

	@Override
	public void fecharSala(Long idSala) {
	
		List<HistoricoSalaUsuario> historicoUsuariosDaSala = historicoRepository.findByIDSala(idSala);
	
		for (HistoricoSalaUsuario historicoUsuario : historicoUsuariosDaSala) {
			//TODO controle transactional se remover um usuario falhar	
			if (historicoUsuario.getData_saida() == null) {
				fecharContaDoUsuario(idSala, historicoUsuario.getPerfil().getUsuario().getId());
			}
		}
		
		this.desativaSala(idSala);
	}
	
	private  void desativaSala(Long idSala) {
		
		Sala sala = this.buscaSala(idSala);		
	
		sala.setAberta(false);
		salaRepository.save(sala);
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
	public ResponseEntity<?> entraSala(Long idPerfil, String codigo) {

		Optional<Sala> sala = buscaSalaPeloCodigo(codigo);		
		Perfil perfilEncontrado = perfilService.buscaPeloId(idPerfil);

		if (!sala.isPresent())
			throw new EmptyResultDataAccessException(1);
		
		Boolean usuarioJaEstaNumaSala = this.validaUsuarioJaEstaNumaSala(perfilEncontrado);
		Boolean usuarioNuncaEntroNestaSala = this.validaUsuarioJaEntrouNestaSala(sala.get(), perfilEncontrado);
		Boolean estaSalaEstaAberta = sala.get().getAberta();
		
		if (estaSalaEstaAberta) {
			if (!usuarioJaEstaNumaSala && !usuarioNuncaEntroNestaSala) {
				HistoricoSalaUsuario historicoSalaUsuario = new HistoricoSalaUsuario();
				historicoSalaUsuario.setSala(sala.get());
				historicoSalaUsuario.setPerfil(perfilEncontrado);
				historicoSalaUsuario.setAtivo(true);
				historicoRepository.save(historicoSalaUsuario);
				return ResponseEntity.status(HttpStatus.CREATED).body(historicoSalaUsuario);
			} else {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario-na-sala", null,
						LocaleContextHolder.getLocale());
				List<AppException> erros = Arrays.asList(new AppException(mensagemUsuario, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
			}
		}else {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario.historico", null,
					LocaleContextHolder.getLocale());
			List<AppException> erros = Arrays.asList(new AppException(mensagemUsuario, null));
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
	public BigDecimal fecharContaDoUsuario(Long id, Long idPerfil) {

		Perfil perfil = perfilService.buscaPeloId(idPerfil);

		HistoricoSalaUsuario historicoSalaUsuario = historicoRepository.findBySalaUsuario(id,
				perfil.getId());

		if (historicoSalaUsuario == null) {			
			throw new EmptyResultDataAccessException(1);
			
		} else {
			
			BigDecimal pegaContaDeUmUsuario = pegaContaDeUmUsuario(id, perfil.getId());
			
			if (historicoSalaUsuario.getData_saida() == null) {
				OffsetDateTime timestamp = OffsetDateTime.now();
				historicoSalaUsuario.setData_saida(timestamp);
				historicoSalaUsuario.setTotalParcial(pegaContaDeUmUsuario);
				historicoSalaUsuario.setAtivo(false);
				
				historicoRepository.save(historicoSalaUsuario);
				
				if(this.verificaSeEhUltimoDaSala(id)) {
					this.desativaSala(id);
				}			
				
				return pegaContaDeUmUsuario;
				
			}else {
				throw new EmptyResultDataAccessException("O usuario ja saiu da sala!", 1);
			}
		}

	}

	// TODO trocar para receber pefil id no parametro
	@Override
	public BigDecimal pegaContaDeUmUsuario(Long id, Long idP) {

		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPedido = BigDecimal.ZERO;
		List<TotalPedido> pedidoUsuario = salaRepository.pedidosSalaPorUsuario(id, idP);

		if (pedidoUsuario.isEmpty())
			return new BigDecimal(0);

		Long usuarioPorPedido;
		for (TotalPedido pedido : pedidoUsuario) {
			totalPedido = pedido.getValor().multiply(new BigDecimal(pedido.getQuantidade()));
			usuarioPorPedido = salaRepository.usuariosPorPedido(pedido.getPedido_id());
			totalPedido = totalPedido.divide(new BigDecimal(usuarioPorPedido),2, RoundingMode.HALF_UP);
			total = total.add(totalPedido);
		}
		
		return total;		
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
	public Sala criarSala(Perfil perfil) throws Exception {

		Perfil perfilEncontrado = perfilService.buscaPeloId(perfil.getId());
		
		if(validaUsuarioJaEstaNumaSala(perfilEncontrado)) {
			String msg = messageSource.getMessage("recurso.usuario-na-sala", null,
					LocaleContextHolder.getLocale());
			throw new Exception(msg);
		}
			
		Sala sala = new Sala();
		String codigo = RandomStringUtils.randomAlphanumeric(4);
		sala.setCodigo(codigo);
		Sala salaSalva = salaRepository.save(sala);
		
		this.entraSala(perfilEncontrado.getId(), codigo);
		
		return salaSalva;
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
	
	private boolean verificaSeEhUltimoDaSala(Long salaId) {
	
		List<HistoricoSalaUsuario> findByIDSala = historicoRepository.findUsuariosAtivo(salaId);
		if(findByIDSala.isEmpty())
			return true;
	
		return false;
	}
	
	/**
	 * @return true se já participou desta sala antes 
	 */
	private Boolean validaUsuarioJaEntrouNestaSala(Sala sala, Perfil perfil) {
		HistoricoSalaUsuario findBySalaUsuario = historicoRepository.findBySalaUsuario(sala.getId(), perfil.getId());
		if(findBySalaUsuario != null)
			return true;
		
		return false;
	}
	
	/**
	 * @return true se já está numa sala atualmente
	 */
	private Boolean validaUsuarioJaEstaNumaSala(Perfil perfil) {
		HistoricoSalaUsuario findSalaAtual = historicoRepository.findSalaAtual(perfil.getId());
		if(findSalaAtual != null)
			return true;
	
		return false;
	}
}
