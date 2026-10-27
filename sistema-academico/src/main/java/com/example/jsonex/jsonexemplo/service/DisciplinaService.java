package com.example.jsonex.jsonexemplo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class DisciplinaService {

    private static final Sort ORDEM = Sort.by("nome");

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository,
            MatriculaRepository matriculaRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    // ==========================================
    // LISTAR / BUSCAR
    // ==========================================
    public List<Disciplina> listarTodos() {
        return disciplinaRepository.findAll(ORDEM);
    }

    public List<Disciplina> listarPorCurso(Long cursoId) {
        return disciplinaRepository.findByCursoId(cursoId, ORDEM);
    }

    public Disciplina buscarPorId(Long id) {
        return disciplinaRepository.findById(id).orElse(null);
    }

    // ==========================================
    // VERIFICAR NOME (cadastro e edição)
    // idIgnorar = null no cadastro; id da própria
    // disciplina na edição.
    // ==========================================
    public boolean nomeEmUso(String nome, Long cursoId, Long idIgnorar) {
        if (nome == null || nome.isBlank() || cursoId == null) {
            return false;
        }
        if (idIgnorar == null) {
            return disciplinaRepository
                    .existsByNomeIgnoreCaseAndCursoId(nome.trim(), cursoId);
        }
        return disciplinaRepository
                .existsByNomeIgnoreCaseAndCursoIdAndIdNot(
                        nome.trim(), cursoId, idIgnorar);
    }

    private Curso buscarCurso(Disciplina disciplina) {
        if (disciplina.getCurso() == null
                || disciplina.getCurso().getId() == null) {
            throw new RegraNegocioException("Selecione um curso.");
        }
        return cursoRepository.findById(disciplina.getCurso().getId())
                .orElseThrow(() ->
                        new RegraNegocioException("Curso não encontrado."));
    }

    // ==========================================
    // CADASTRAR
    // ==========================================
    public Disciplina cadastrar(Disciplina disciplina) {
        disciplina.setNome(disciplina.getNome().trim());
        disciplina.setCurso(buscarCurso(disciplina));
        return disciplinaRepository.save(disciplina);
    }

    // ==========================================
    // ATUALIZAR
    // ==========================================
    public Disciplina atualizar(Long id, Disciplina dados) {
        Disciplina existente = buscarPorId(id);
        if (existente == null) {
            return null;
        }
        Curso novoCurso = buscarCurso(dados);

        boolean mudouCurso = !novoCurso.getId()
                .equals(existente.getCurso().getId());
        if (mudouCurso && possuiMatriculas(id)) {
            throw new RegraNegocioException(
                    "Não é possível trocar o curso de uma disciplina que "
                    + "possui alunos matriculados.");
        }

        existente.setNome(dados.getNome().trim());
        existente.setCargaHoraria(dados.getCargaHoraria());
        existente.setCurso(novoCurso);
        return disciplinaRepository.save(existente);
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    public boolean possuiMatriculas(Long id) {
        return matriculaRepository.existsByDisciplinaId(id);
    }

    public boolean excluir(Long id) {
        // Não permite excluir disciplina que possui matrículas.
        if (!disciplinaRepository.existsById(id) || possuiMatriculas(id)) {
            return false;
        }
        disciplinaRepository.deleteById(id);
        return true;
    }
}
