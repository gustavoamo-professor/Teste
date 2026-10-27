package com.example.jsonex.jsonexemplo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CursoService(
            CursoRepository cursoRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository) {
        this.cursoRepository = cursoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll(Sort.by("nome"));
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    /** idIgnorar = null no cadastro; id do próprio curso na edição. */
    public boolean nomeEmUso(String nome, Long idIgnorar) {
        if (nome == null || nome.isBlank()) {
            return false;
        }
        if (idIgnorar == null) {
            return cursoRepository.existsByNomeIgnoreCase(nome.trim());
        }
        return cursoRepository.existsByNomeIgnoreCaseAndIdNot(
                nome.trim(), idIgnorar);
    }

    public Curso cadastrar(Curso curso) {
        curso.setNome(curso.getNome().trim());
        return cursoRepository.save(curso);
    }

    public Curso atualizar(Long id, Curso dados) {
        Curso existente = buscarPorId(id);
        if (existente == null) {
            return null;
        }
        existente.setNome(dados.getNome().trim());
        return cursoRepository.save(existente);
    }

    public boolean possuiVinculos(Long id) {
        return alunoRepository.existsByCursoId(id)
                || disciplinaRepository.existsByCursoId(id);
    }

    public boolean excluir(Long id) {
        if (!cursoRepository.existsById(id) || possuiVinculos(id)) {
            return false;
        }
        cursoRepository.deleteById(id);
        return true;
    }
}
