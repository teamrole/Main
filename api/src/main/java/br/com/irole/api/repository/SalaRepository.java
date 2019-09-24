package br.com.irole.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.irole.api.model.Sala;
import br.com.irole.api.model.Usuario;

public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public Optional<Sala> findByCodigo(String codigo);
	
}
