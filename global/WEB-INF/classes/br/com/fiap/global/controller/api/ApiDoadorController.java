package br.com.fiap.global.controller.api;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.fiap.global.model.Doador;
import br.com.fiap.global.repository.DoadorRepository;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/doador")
@Slf4j
public class ApiDoadorController {
	
	@Autowired
	private DoadorRepository repository;
	
	@GetMapping
	@Cacheable("doadores")
	public Page<Doador> index(
			@RequestParam(required = false) String title,
			@PageableDefault(size = 20) Pageable pageable
			) {
		
		if (title == null)
			return repository.findAll(pageable);
		
		//TODO usar contains
		return repository.findByTitleLike("%" + title + "%", pageable);
	}
	
	@PostMapping
	@CacheEvict(value = "doadores", allEntries = true)
	public ResponseEntity<Doador> create(@RequestBody @Valid Doador doador, UriComponentsBuilder uriBuilder) {
		repository.save(doador);
		URI uri = uriBuilder
				.path("/api/doador/{id}")
				.buildAndExpand(doador.getId())
				.toUri();
		return ResponseEntity.created(uri).body(doador);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Doador> get(@PathVariable Long id) {
		return ResponseEntity.of(repository.findById(id));
	}
	
	@DeleteMapping("{id}")
	@CacheEvict(value = "doadores", allEntries = true)
	public ResponseEntity<Doador> delete(@PathVariable Long id){
		Optional<Doador> doador = repository.findById(id);
		
		if(doador.isEmpty()) 
			return ResponseEntity.notFound().build();
		
		repository.deleteById(id);
		
		return ResponseEntity.ok().build();
		
	}

	@PutMapping("{id}")
	@CacheEvict(value = "doadores", allEntries = true)
	public ResponseEntity<Doador> update(@RequestBody @Valid Doador newDoador, @PathVariable Long id ) {
		Optional<Doador> optional = repository.findById(id);
		
		if(optional.isEmpty()) 
			return ResponseEntity.notFound().build();
		
		Doador doador = optional.get();
		
		doador.setTitle(newDoador.getTitle());
		doador.setDescription(newDoador.getDescription());
		//comerciante.setPoints(newComerciante.getPoints());
		
		repository.save(doador);
		
		log.info("Doador id=" + id +" alterado para " + doador.toString());

		return ResponseEntity.ok(doador);

		
	}
	
	
	
	
	
	
	
	
	
	

}
