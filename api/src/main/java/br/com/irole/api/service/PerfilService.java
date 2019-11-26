
package br.com.irole.api.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;

import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;

public interface PerfilService {
	
	/**
	 * Encontra um perfil
	 * @param id o ID do perfil a ser encontrado
	 * @throws EmptyResultDataAccessException que é tratado internamente caso um pefil não seja encontrado
	 * @return Perfil
	 */
	public Perfil buscaPeloId(Long id);

	/**
	 * Encontra um perfil
	 * @param id o ID do usuário que é dono do perfil a ser encontrado
	 * @return ResponseEntity<Perfil> ou ResponseEntity com uma lista de erros
	 */
	public ResponseEntity<?> buscaPerfilPeloUsuario(Long id);
	
	/**
	 * Altera um perfil
	 * @param id o ID do perfil a ser alterado
	 * @param perfil um objeto contendo os novos valores
	 * @return Perfil
	 */
	public Perfil atualizar(Long id, Perfil perfil);
	
	/**
	 * Cadastrar um perfil ao usuário recém criado, se der erro deve alertar o Usuario Service
	 * para que o mesmo possa fazer um rollback
	 * @param usuario um objeto Usuário que vai receber um perfil
	 * @throws Exception uma exeção genérica que alerta que o cadastro do perfil falhou
	 * @return Perfil
	 */
	public Perfil cadastraPerfilParaUsuario(Usuario usuario) throws Exception;
	
}
