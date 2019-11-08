package br.com.irole.api.resource;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	//TODO tornar cacheavel
	public ResponseEntity<List<ItemTipoPacote>> getTipoItems() {
		ItemTipo[] enums = ItemTipo.values();
	
		List<ItemTipoPacote> collect = IntStream.range(0, enums.length)  
			.mapToObj(index -> {
				ItemTipoPacote tipoItem = new ItemTipoPacote(index+1, enums[index].toString());
				return tipoItem;
			})
			.collect(Collectors.toList());
		
		return (collect != null) ? ResponseEntity.ok(collect) : ResponseEntity.noContent().build();
	}
	
	private  static class ItemTipoPacote implements Serializable{
		private static final long serialVersionUID = 1L;
		int id;
		String value;

		public ItemTipoPacote(int index, String value) {
			this.id = index;
			this.value = value;
		}
		
		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}
	}
	
}
