package br.com.irole.api.repository.customizado;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.irole.api.repository.UsuarioRepository;

public interface IUsuarioPermissao extends UsuarioRepository{
	
	@Query(value="SELECT * FROM usuario_permissao where usuario_id = :u and permissao_id = :p", nativeQuery=true)
	public UsuarioPermissao findUsuarioPermissaoByUserAndPermissao(
			@Param("u") Long user_id, @Param("p") Long permissao_id);	
	
	@Modifying
	@Query(value="DELETE from usuario_permissao where usuario_id = :u and permissao_id = :p", nativeQuery=true)
	public void deleteByUserAndPermissao(
			@Param("u") Long user_id, @Param("p") Long permissao_id);
	
	
	public static interface UsuarioPermissao {	
	     Long getUsuario_id();
	
	     Long getPermissao_id();
	}
}
