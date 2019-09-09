package br.com.irole.api.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testes")
public class TesteResource {
	
	List<String> valores = new ArrayList<>(Arrays.asList(
	    "Hello",
	    "world",
	    "Daniel",
	    "Junior"
	));
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_LISTAR_TESTES')")
	public ResponseEntity<?> lista(){
		return ResponseEntity.ok(valores);
	}
}
