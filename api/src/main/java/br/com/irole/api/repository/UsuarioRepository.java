package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.irole.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
}
