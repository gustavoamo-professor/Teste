package com.example.jsonex.jsonexemplo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, MatriculaRepository matriculaRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Disciplina> listarTodos() {
        return disciplinaRepository.findAll();
    }

    public Disciplina buscarPorId(Long id) {
        return disciplinaRepository.findById(id).orElse(null);
    }

    public boolean nomeJaCadastrado(String nome) {
        if (nome == null || nome.isBlank()) {
            return false;
        }
        return disciplinaRepository.existsByNomeIgnoreCase(nome.trim());
    }

    public boolean nomeJaCadastradoPorOutra(String nome, Long id) {
        if (nome == null || nome.isBlank()) {
            return false;
        }
        return disciplinaRepository.existsByNomeIgnoreCaseAndIdNot(nome.trim(), id);
    }

    public Disciplina cadastrar(Disciplina disciplina) {
        disciplina.setNome(disciplina.getNome().trim());
        return disciplinaRepository.save(disciplina);
    }

    public Disciplina atualizar(Long id, Disciplina disciplinaAtualizada) {
        Disciplina disciplinaExistente = buscarPorId(id);
        if (disciplinaExistente == null) {
            return null;
        }
        disciplinaExistente.setNome(disciplinaAtualizada.getNome().trim());
        disciplinaExistente.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());
        disciplinaExistente.setCurso(disciplinaAtualizada.getCurso());
        return disciplinaRepository.save(disciplinaExistente);
    }

    public boolean possuiMatriculas(Long id) {
        return matriculaRepository.existsByDisciplinaId(id);
    }

    public boolean excluir(Long id) {
        if (!disciplinaRepository.existsById(id) || possuiMatriculas(id)) {
            return false;
        }
        disciplinaRepository.deleteById(id);
        return true;
    }
}