package br.com.irole.api.service;

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

import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.PerfilRepository;
import br.com.irole.api.repository.UsuarioRepository;


@Service
public class PerfilService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	
	public Perfil atualizar(Long id, Perfil perfil) {
		Optional<Perfil> buscaPerfil = perfilRepository.findById(id);
		if (buscaPerfil.isPresent()) {
			BeanUtils.copyProperties(perfil, buscaPerfil, "id");
			perfilRepository.save(buscaPerfil.get());
			return buscaPerfil.get();			
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	
	public ResponseEntity<?> buscaPerfilId(Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			if(usuario.get().getAtivo()) {
				Perfil perfil = perfilRepository.findByUsuario(usuario.get());
				if (perfil != null) {
					return ResponseEntity.ok(perfil);
				}else {
					return ResponseEntity.notFound().build();
				}
			}else {
				String mensagemUsuario = messageSource.getMessage("recurso.usuario-inativo", null, LocaleContextHolder.getLocale());
				List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
			}
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	public static class Erro{
		
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagemUsuario) {
			this.mensagemUsuario = mensagemUsuario;
		}
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}
		
		
	}
}
