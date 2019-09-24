package br.com.irole.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.irole.api.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
