package br.com.irole.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.irole.api.model.ItemTipo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/itens")
public class ItemController {

	@GetMapping("/tipos")
	@ApiOperation(notes = "Retorna uma lista de tipo de items", value = "Capturar tipo de items")
	public  ResponseEntity<ItemTipo[]>getTipoItems() {

		return (ItemTipo.values() != null) ? ResponseEntity.ok(ItemTipo.values()) : ResponseEntity.noContent().build();
	}
}
