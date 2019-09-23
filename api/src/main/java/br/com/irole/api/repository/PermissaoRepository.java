package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.irole.api.model.Permissao;

@Service
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
		
}
