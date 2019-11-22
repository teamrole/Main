package br.com.irole.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Sala;

public interface SalaService {
	
	/**
	 * Encontra uma sala pelo id
	 * @param id o ID da sala
	 * @return Sala
	 */
	public Sala buscaSala(Long id);
	
	/**
	 * Encontra uma sala pelo código público
	 * @param codigo o Codigo gerado quando uma sala é criada
	 * @return Optional<Sala>
	 */
	public Optional<Sala> buscaSalaPeloCodigo(String codigo);
	
	/**
	 * Cria uma nova sala e gera um código público para que novos membros possam encontrar a sala
	 * @return Sala
	 */
	public Sala criarSala();
	
	/**
	 * Entra em uma sala usando um código público
	 * @param idUsuario o ID do usuário que vai entrar na sala
	 * @param codigo o Codigo público que identifica a sala
	 * @return Optional<Sala>
	 */
	public ResponseEntity<?> entraSala(Long idUsuario, String codigo);

	/**
	 * Fechará a sala, usuários serão removidos e não poderão entrar novamente
	 * @param id o ID da sala
	 */
	public void fecharSala(Long id);
	
	/**
	 * Mostra todos os participantes de uma sala
	 * @param id o ID da sala
	 * @return List<HistoricoSalaUsuario>
	 */
	public List<HistoricoSalaUsuario> usuariosSala(Long id);
	
	/**
	 * Remove um usuário específico da sala
	 * @param id o ID da sala
	 * @param idUsuario o ID do usuario
	 * @return BigDecimal o valor total gasto pelo usuário naquela sala
	 */
	public BigDecimal fecharContaDoUsuario(Long id, Long idUsuario);
	
	/**
	 * Calcula os gastos de um usuário específico da sala
	 * @param id o ID da sala
	 * @param idUsuario o ID do usuario
	 * @return BigDecimal o valor total gasto pelo usuário naquela sala
	 */
	public BigDecimal pegaContaDeUmUsuario(Long id, Long idU);
	
	/**
	 * Altera o nome da sala
	 * @param id o ID da sala
	 * @param nomeDaSala o novo nome da sala
	 * @return ResponseEntity<Sala> ou ResponseEntity com uma lista de erros
	 */
	public ResponseEntity<?> editarNomeSala(Long id, String nomeDaSala);
	
	
}
