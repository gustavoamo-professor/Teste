package com.example.jsonex.jsonexemplo.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.jsonex.jsonexemplo.dto.MatriculaDTO;
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
    public MatriculaService(MatriculaRepository matriculaRepository, AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }
    public List<Matricula> listarTodos() { return matriculaRepository.findAll(); }
    public Matricula buscarPorId(Long id) { return matriculaRepository.findById(id).orElse(null); }
    public MatriculaDTO buscarDTOporId(Long id) {
        Matricula matricula = buscarPorId(id);
        if (matricula == null) return null;

        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(matricula.getId());

        Aluno aluno = (Aluno) matricula.getAluno();
        Disciplina disciplina = (Disciplina) matricula.getDisciplina();

        dto.setAlunoId(aluno != null ? aluno.getId() : null);
        dto.setDisciplinaId(disciplina != null ? disciplina.getId() : null);
        return dto;
    }
    public boolean matriculaJaExiste(Long alunoId, Long disciplinaId) {
        if (alunoId == null || disciplinaId == null) return false;
        return matriculaRepository.existsByAlunoIdAndDisciplinaId(alunoId, disciplinaId);
    }
    public boolean matriculaJaExisteEmOutra(Long alunoId, Long disciplinaId, Long id) {
        if (alunoId == null || disciplinaId == null) return false;
        return matriculaRepository.existsByAlunoIdAndDisciplinaIdAndIdNot(alunoId, disciplinaId, id);
    }
    public boolean alunoEDisciplinaDoMesmoCurso(Long alunoId, Long disciplinaId) {
        if (alunoId == null || disciplinaId == null) return false;
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId).orElse(null);
        if (aluno == null || disciplina == null || aluno.getCurso() == null || disciplina.getCurso() == null) return false;
        return aluno.getCurso().getId().equals(disciplina.getCurso().getId());
    }
    public Matricula cadastrar(MatriculaDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId()).orElse(null);
        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId()).orElse(null);
        if (aluno == null || disciplina == null) return null;
        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
        return matriculaRepository.save(matricula);
    }
    public Matricula atualizar(Long id, MatriculaDTO dto) {
        Matricula matriculaExistente = buscarPorId(id);
        if (matriculaExistente == null) return null;
        Aluno aluno = alunoRepository.findById(dto.getAlunoId()).orElse(null);
        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId()).orElse(null);
        if (aluno == null || disciplina == null) return null;
        matriculaExistente.setAluno(aluno);
        matriculaExistente.setDisciplina(disciplina);
        return matriculaRepository.save(matriculaExistente);
    }
    public boolean excluir(Long id) {
        if (!matriculaRepository.existsById(id)) return false;
        matriculaRepository.deleteById(id);
        return true;
    }
}