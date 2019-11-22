package br.com.irole.api.service;

import org.springframework.http.ResponseEntity;

import br.com.irole.api.model.Usuario;

public interface UsuarioService {
	
	/**
	 * faz login
	 * @param usuario um objeto Usuario
	 * @return Usuario o usuario logado ou "null" caso o login falhe
	 */
	public Usuario login(Usuario usuario);
	
	/**
	 * Faz cadastro de um novo usuário
	 * @param usuario um objeto Usuario a ser cadastrado
	 * @return ResponseEntity<Usuario> ou ResponseEntity com uma lista de erros
	 */
	public ResponseEntity<?> cadastra(Usuario usuario);
	
	/**
	 * Flag para ativar ou desativar a conta de um usuário 
	 * @param id o ID do usuário
	 * @param status do tipo Boolean, true para ativo e false para desativo
	 */
	public void atualizarAtivo(Long id, Boolean ativo);
	
	/**
	 * Altera os dados do usuário
	 * @param id o ID do usuário
	 * @param usuario um objeto Usuário contendo os novos valores
	 * @param status do tipo Boolean, true para ativo e false para desativo
	 */
	public Usuario atualizar(Long id, Usuario usuario);
	
	/**
	 * Busca um usuário
	 * @param id o ID do usuário
	 * @return Usuario
	 */
	public Usuario buscaUsuario(Long id);
}
