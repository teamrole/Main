package br.com.irole.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.model.Permissao;
import br.com.irole.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByCelular(String celular);
	
	@Query("SELECT u.permissao FROM Usuario u WHERE u.id = :user_id")
	public List<Permissao> findPermissaoByUsuarioID(@Param("user_id") Long user_id);
}
