package br.com.irole.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.irole.api.model.HistoricoSalaUsuario;
import br.com.irole.api.model.Perfil;
import br.com.irole.api.model.Usuario;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
	
	public Perfil findByUsuario(Usuario id);	
	
	
}
