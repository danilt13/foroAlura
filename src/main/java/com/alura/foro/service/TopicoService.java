package com.alura.foro.service;

import com.alura.foro.model.Topico;
import com.alura.foro.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Transactional
    public Topico crearTopico(Topico topico) {
        if (topicoRepository.existsByTituloAndMensaje(topico.getTitulo(), topico.getMensaje())) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje");
        }
        return topicoRepository.save(topico);
    }

    public List<Topico> obtenerTodosLosTopicos() {
        return topicoRepository.findAll();
    }

    public Optional<Topico> obtenerTopicoPorId(Long id) {
        return topicoRepository.findById(id);
    }

    public Topico actualizarTopico(Long id, Topico topicoActualizado) {
        Optional<Topico> optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isPresent()) {
            Topico topico = optionalTopico.get();
            // Actualizar los campos del tópico con los datos proporcionados
            topico.setTitulo(topicoActualizado.getTitulo());
            topico.setMensaje(topicoActualizado.getMensaje());
            topico.setAutor(topicoActualizado.getAutor());
            topico.setCurso(topicoActualizado.getCurso());
            // Guardar y devolver el tópico actualizado
            return topicoRepository.save(topico);
        } else {
            throw new IllegalArgumentException("No se encontró el tópico con ID: " + id);
        }
    }

    public void eliminarTopico(Long id) {
        try {
            topicoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("El tópico con ID " + id + " no existe");
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar el tópico con ID " + id, ex);
        }
    }
}
