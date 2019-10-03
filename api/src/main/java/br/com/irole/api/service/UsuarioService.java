package br.com.irole.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario atualizar(Long id, Usuario usuario) {
		Usuario buscaUsuario = buscaUsuario(id);
		BeanUtils.copyProperties(usuario, buscaUsuario, "id");
		return usuarioRepository.save(buscaUsuario);	
	}

	public void atualizarAtivo(Long id, Boolean ativo) {
		Usuario buscaUsuario = buscaUsuario(id);
		buscaUsuario.setAtivo(ativo);
		usuarioRepository.save(buscaUsuario);
		
	}
	
	public Usuario buscaUsuario(Long id) {
		Optional<Usuario> usuarioSalvo = usuarioRepository.findById(id);		
		if (usuarioSalvo.isPresent()) {
			return usuarioSalvo.get();
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}
	
}
