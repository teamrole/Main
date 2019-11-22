package br.com.irole.api.service.implementation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.irole.api.exceptionhandler.ExceptionHandler.Erro;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.repository.UsuarioRepository;
import br.com.irole.api.service.PerfilService;

@Service
public class PerfilServiceImpl implements PerfilService{

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private UsuarioServiceImpl usuarioService;

	@Autowired
	private PerfilRepository perfilRepository;

	@Override
	public Perfil atualizar(Long id, Perfil perfil) {
		Optional<Perfil> buscaPerfil = perfilRepository.findById(id);
		if (buscaPerfil.isPresent()) {
			BeanUtils.copyProperties(perfil, buscaPerfil, "id");
			perfilRepository.save(buscaPerfil.get());
			return buscaPerfil.get();
		} else {
			throw new EmptyResultDataAccessException(1);
		}
	}

	@Override
	public ResponseEntity<?> buscaPerfil(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			if (usuario.get().getAtivo()) {
				Perfil perfil = perfilRepository.findByUsuario(usuario.get());
				if (perfil != null) {
					return ResponseEntity.ok(perfil);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario-inativo", null,
						LocaleContextHolder.getLocale());
				List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public Perfil cadastraPerfilParaUsuario(Usuario usuario) throws Exception {

		Perfil perfil = new Perfil();	
		
		Optional<Usuario> usuariovalidado = usuarioRepository.findById(usuario.getId());
		
		if(!usuariovalidado.isPresent() || !usuariovalidado.get().getAtivo())
			throw new Exception("usuario erro");
			
			perfil.setUsuario(usuariovalidado.get());
		
		try {
			Perfil perfilSalvo = perfilRepository.save(perfil);
			
			if(perfilSalvo == null)
				throw new Exception("perfil erro");	
			
			return perfilSalvo;
		}catch (Exception e) {
			usuarioService.rollbackUsuarioPerfil(usuariovalidado.get());
			throw e;
		}
	}

}
