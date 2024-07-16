package com.alura.foro.controller;

import com.alura.foro.model.Topico;
import com.alura.foro.repository.TopicoRepository;
import com.alura.foro.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping("/crear")
    public ResponseEntity<?> crearTopico(@Valid @RequestBody Topico topico, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError error : result.getFieldErrors()) {
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body("Errores de validación: " + sb.toString());
        }
        try {
            Topico topicoCreado = topicoService.crearTopico(topico);
            return new ResponseEntity<>(topicoCreado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Topico>> obtenerTodosLosTopicos() {
        List<Topico> topicos = topicoService.obtenerTodosLosTopicos();
        return new ResponseEntity<>(topicos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Topico>> listarTodosLosTopicos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer año) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").ascending());

        if (curso != null && año != null) {
            int year = año.intValue();
            List<Topico> topicos = topicoRepository.findAllByCursoAndFechaCreacionYear(curso, year, pageable);
            return ResponseEntity.ok(topicos);
        } else {
            Page<Topico> topicosPage = topicoRepository.findAll(pageable);
            return ResponseEntity.ok(topicosPage.getContent());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleTopico(@PathVariable Long id) {
        Optional<Topico> optionalTopico = topicoService.obtenerTopicoPorId(id);

        if (optionalTopico.isPresent()) {
            Topico topico = optionalTopico.get();
            return ResponseEntity.ok(topico);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTopico(
            @PathVariable Long id,
            @Valid @RequestBody Topico topicoActualizado,
            BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError error : result.getFieldErrors()) {
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body("Errores de validación: " + sb.toString());
        }
        try {
            Topico topico = topicoService.actualizarTopico(id, topicoActualizado);
            return ResponseEntity.ok(topico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        try {
            topicoService.eliminarTopico(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el tópico: " + e.getMessage());
        }
    }

}

