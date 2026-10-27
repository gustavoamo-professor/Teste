package com.example.jsonex.jsonexemplo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public Curso salvar(Curso curso) {
        return cursoRepository.save(curso);
    }
}