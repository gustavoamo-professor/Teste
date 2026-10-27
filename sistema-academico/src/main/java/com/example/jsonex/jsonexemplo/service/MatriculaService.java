package com.example.jsonex.jsonexemplo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.model.Matricula;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class MatriculaService {

    private static final Sort ORDEM = Sort.by("aluno.nome", "disciplina.nome");

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
        return matriculaRepository.findAll(ORDEM);
    }

    public List<Matricula> listarPorAluno(Long alunoId) {
        return matriculaRepository.findByAlunoId(alunoId, ORDEM);
    }

    public List<Matricula> listarPorDisciplina(Long disciplinaId) {
        return matriculaRepository.findByDisciplinaId(disciplinaId, ORDEM);
    }

    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id).orElse(null);
    }

    /**
     * Matricula o aluno na disciplina.
     * Lança RegraNegocioException (em vez de devolver null em silêncio)
     * para que a tela mostre o motivo.
     */
    public Matricula matricular(
            Long alunoId,
            Long disciplinaId,
            Double nota,
            Integer frequencia) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() ->
                        new RegraNegocioException("Aluno não encontrado."));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() ->
                        new RegraNegocioException("Disciplina não encontrada."));

        // Curso → Alunos → Matrículas → Disciplinas:
        // o aluno só cursa disciplinas do próprio curso.
        if (!aluno.getCurso().getId().equals(disciplina.getCurso().getId())) {
            throw new RegraNegocioException(
                    "A disciplina \"" + disciplina.getNome()
                    + "\" pertence ao curso \"" + disciplina.getCursoNome()
                    + "\", mas o aluno está no curso \""
                    + aluno.getCursoNome() + "\".");
        }

        // Evita matrícula duplicada
        if (matriculaRepository.existsByAlunoIdAndDisciplinaId(
                alunoId, disciplinaId)) {
            throw new RegraNegocioException(
                    "O aluno já está matriculado nesta disciplina.");
        }

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
        matricula.setNota(nota);
        matricula.setFrequencia(frequencia);
        return matriculaRepository.save(matricula);
    }

    /** Só nota e frequência podem ser alteradas. */
    public Matricula atualizar(Long id, Double nota, Integer frequencia) {
        Matricula existente = buscarPorId(id);
        if (existente == null) {
            return null;
        }
        existente.setNota(nota);
        existente.setFrequencia(frequencia);
        return matriculaRepository.save(existente);
    }

    public boolean excluir(Long id) {
        if (!matriculaRepository.existsById(id)) {
            return false;
        }
        matriculaRepository.deleteById(id);
        return true;
    }
}
