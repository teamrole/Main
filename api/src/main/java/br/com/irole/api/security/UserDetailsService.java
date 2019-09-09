package br.com.irole.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Usuario;
import br.com.irole.api.repository.UsuarioRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String celular) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByCelular(celular);
		Usuario usuario =
				usuarioOptional.orElseThrow(()-> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		return new User(celular, usuario.getSenha(), getPermissao(usuario));
	}
	
	private Collection<? extends GrantedAuthority> getPermissao(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissao().forEach(p->authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		return authorities;
	}
}
