package com.alura.foro.repository;

import com.alura.foro.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
    List<Topico> findAllByCursoAndFechaCreacionYear(String curso, int year, Pageable pageable);

    Page<Topico> findAll(Pageable pageable);
}
