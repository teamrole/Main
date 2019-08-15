package br.com.irole.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class HomeController {
	
	@GetMapping
	public ResponseEntity<?> helloWorld(){
		return ResponseEntity.ok("hello world");
	}
}
