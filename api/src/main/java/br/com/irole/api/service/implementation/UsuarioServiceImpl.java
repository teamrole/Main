package br.com.irole.api.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.UsuarioRepository;
import br.com.irole.api.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PerfilServiceImpl perfilService;

	@PersistenceContext
	private EntityManager em;

	@Override
	public Usuario atualizar(Long id, Usuario usuario) {
		Usuario buscaUsuario = buscaUsuario(id);
		BeanUtils.copyProperties(usuario, buscaUsuario, "id");
		return usuarioRepository.save(buscaUsuario);
	}

	@Override
	public void atualizarAtivo(Long id, Boolean ativo) {
		Usuario buscaUsuario = buscaUsuario(id);
		buscaUsuario.setAtivo(ativo);
		usuarioRepository.save(buscaUsuario);

	}

	@Override
	public Usuario buscaUsuario(Long id) {
		Optional<Usuario> usuarioSalvo = usuarioRepository.findById(id);
		if (usuarioSalvo.isPresent()) {
			return usuarioSalvo.get();
		} else {
			throw new EmptyResultDataAccessException(1);
		}
	}

	@Override
	public Usuario login(Usuario usuario) {
		Optional<Usuario> findByCelular = usuarioRepository.findByCelular(usuario.getCelular());

		// celular nao cadastrado
		if (findByCelular == null)
			return null;

		// senha nao bate com o do usuario encontrado pelo celular
		if (!bCryptPasswordEncoder.matches(usuario.getSenha(), findByCelular.get().getSenha()))
			return null;

		return findByCelular.get();
	}
   
	@Transactional
	public Usuario cadastraUsuarioEPerfil(@Valid Usuario usuario) throws Exception  {

		usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));

		Usuario novoUsuario = null;
		
		novoUsuario = usuarioRepository.save(usuario);
		if(novoUsuario == null) {
			throw new Exception("erro usuario");
		}else {
			Perfil perfilCadastrado = perfilService.cadastraPerfilParaUsuario(novoUsuario);
			novoUsuario.setPerfil(perfilCadastrado);
		}		
		 return novoUsuario;
	}

	@Override
	public ResponseEntity<?> cadastra(@Valid Usuario usuario) {
		List<Erro> erros = new ArrayList<>();
		
		if(this.verificaCelularExiste(usuario.getCelular())) {
			String mensagemUsuario = messageSource.getMessage("recurso.usuario.celular-existe", new Object[] {usuario.getCelular()},
					LocaleContextHolder.getLocale());
			return ResponseEntity.badRequest().body(new Erro(mensagemUsuario, mensagemUsuario));
		}

		try {
			Usuario usuarioCadastrado = cadastraUsuarioEPerfil(usuario);

			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCadastrado);

		} catch (Exception e) {
			if (e.getMessage().equals("erro usuario")) {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario.usuario", null,
						LocaleContextHolder.getLocale());
				String mensagemDev = e.getMessage();
				Erro erro = new Erro(mensagemUsuario, mensagemDev);
				erros.add(erro);
			} else if (e.getMessage().equals("erro perfil")) {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario.perfil", null,
						LocaleContextHolder.getLocale());
				String mensagemDev = e.getMessage();
				Erro erro = new Erro(mensagemUsuario, mensagemDev);
				erros.add(erro);
			} else {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario.transacao", null,
						LocaleContextHolder.getLocale());
				String mensagemDev = e.getMessage();
				Erro erro = new Erro(mensagemUsuario, mensagemDev);
				erros.add(erro);
			}
			return ResponseEntity.badRequest().body(erros);
		}

	}

	private Boolean verificaCelularExiste(String celular) {
		Optional<Usuario> usuario = this.usuarioRepository.findByCelular( celular);
		if(usuario.isPresent())
			return true;
		
		return false;
	}

	public void rollbackUsuarioPerfil(@Valid Usuario usuario) {
		Optional<Usuario> usuariovalidado = usuarioRepository.findById(usuario.getId());
		if(usuariovalidado.isPresent()) {
			Long id = usuariovalidado.get().getId();
			usuarioRepository.deletar(id);
		}		
	}
}
