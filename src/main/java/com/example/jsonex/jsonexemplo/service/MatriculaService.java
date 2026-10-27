package com.example.jsonex.jsonexemplo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.model.Matricula;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Matricula> listarTodos() {
        return matriculaRepository.findAll();
    }

    public List<Matricula> listarPorAluno(Long alunoId) {
        return matriculaRepository.findByAlunoId(alunoId);
    }

    public Matricula matricular(Long alunoId, Long disciplinaId) {
        if (matriculaRepository.existsByAlunoIdAndDisciplinaId(alunoId, disciplinaId)) {
            throw new IllegalArgumentException("O aluno já está matriculado nesta disciplina.");
        }

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada."));

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        return matriculaRepository.save(matricula);
    }

    public boolean excluir(Long id) {
        if (!matriculaRepository.existsById(id)) {
            return false;
        }
        matriculaRepository.deleteById(id);
        return true;
    }
}