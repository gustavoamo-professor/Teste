package com.example.jsonex.jsonexemplo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class AlunoService {

    private static final Sort ORDEM = Sort.by("nome");

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            CursoRepository cursoRepository,
            MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll(ORDEM);
    }

    public List<Aluno> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }
        return alunoRepository.findByNomeContainingIgnoreCase(
                nome.trim(), ORDEM);
    }

    public List<Aluno> listarPorCurso(Long cursoId) {
        return alunoRepository.findByCursoId(cursoId, ORDEM);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public boolean emailEmUso(String email, Long idIgnorar) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (idIgnorar == null) {
            return alunoRepository.existsByEmailIgnoreCase(email.trim());
        }
        return alunoRepository.existsByEmailIgnoreCaseAndIdNot(
                email.trim(), idIgnorar);
    }

    public boolean raEmUso(String ra, Long idIgnorar) {
        if (ra == null || ra.isBlank()) {
            return false;
        }
        if (idIgnorar == null) {
            return alunoRepository.existsByRaIgnoreCase(ra.trim());
        }
        return alunoRepository.existsByRaIgnoreCaseAndIdNot(
                ra.trim(), idIgnorar);
    }

    private Curso buscarCurso(Aluno aluno) {
        if (aluno.getCurso() == null || aluno.getCurso().getId() == null) {
            throw new RegraNegocioException("Selecione um curso.");
        }
        return cursoRepository.findById(aluno.getCurso().getId())
                .orElseThrow(() ->
                        new RegraNegocioException("Curso não encontrado."));
    }

    public Aluno cadastrar(Aluno aluno) {
        aluno.setNome(aluno.getNome().trim());
        aluno.setEmail(aluno.getEmail().trim());
        aluno.setRa(aluno.getRa().trim());
        aluno.setCurso(buscarCurso(aluno));
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Long id, Aluno dados) {
        Aluno existente = buscarPorId(id);
        if (existente == null) {
            return null;
        }
        Curso novoCurso = buscarCurso(dados);

        // Mudar de curso com matrículas deixaria disciplinas
        // de outro curso ligadas ao aluno.
        boolean mudouCurso = !novoCurso.getId()
                .equals(existente.getCurso().getId());
        if (mudouCurso && possuiMatriculas(id)) {
            throw new RegraNegocioException(
                    "Não é possível trocar o curso de um aluno que possui "
                    + "matrículas. Exclua as matrículas antes.");
        }

        existente.setNome(dados.getNome().trim());
        existente.setIdade(dados.getIdade());
        existente.setEmail(dados.getEmail().trim());
        existente.setRa(dados.getRa().trim());
        existente.setCurso(novoCurso);
        return alunoRepository.save(existente);
    }

    public boolean possuiMatriculas(Long id) {
        return matriculaRepository.existsByAlunoId(id);
    }

    public boolean excluir(Long id) {
        if (!alunoRepository.existsById(id) || possuiMatriculas(id)) {
            return false;
        }
        alunoRepository.deleteById(id);
        return true;
    }
}
