package com.projetointegrador.rainhadastortas.controller;

import com.projetointegrador.rainhadastortas.model.Torta;
import com.projetointegrador.rainhadastortas.repository.CategoriaRepository;
import com.projetointegrador.rainhadastortas.repository.TortaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tortas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TortaController {

	@Autowired
	private TortaRepository tortaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Torta>> getAll() {
		return ResponseEntity.ok(tortaRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Torta> getById(@PathVariable Long id) {
		return tortaRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Torta> post(@Valid @RequestBody Torta torta) {
		if (categoriaRepository.existsById(torta.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(tortaRepository.save(torta));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não Existe", null);
	}

	@PutMapping
	public ResponseEntity<Torta> put(@Valid @RequestBody Torta torta) {

		if (tortaRepository.existsById(torta.getId())) {
			if (categoriaRepository.existsById(torta.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(tortaRepository.save(torta));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não Existe", null);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Torta> torta = tortaRepository.findById(id);

		if (torta.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		tortaRepository.deleteById(id);
	}
}
