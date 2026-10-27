package com.example.jsonex.jsonexemplo.service;

import com.example.jsonex.jsonexemplo.dto.AlunoDTO;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public List<AlunoDTO> pesquisarPorNome(String nome) {
        List<Aluno> alunos;

        if (nome != null && !nome.isBlank()) {
            alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            alunos = alunoRepository.findAll();
        }

        return alunos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void cadastrar(AlunoDTO dto) {
        Aluno aluno = converterParaEntidade(dto);
        alunoRepository.save(aluno);
    }

    public void atualizar(Long id, AlunoDTO dto) {
        Aluno alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        alunoExistente.setNome(dto.getNome());
        alunoExistente.setIdade(dto.getIdade());
        alunoExistente.setEmail(dto.getEmail());
        alunoExistente.setMatricula(dto.getMatricula());

        if (dto.getCursoId() != null) {
            Curso curso = cursoRepository.findById(dto.getCursoId()).orElse(null);
            alunoExistente.setCurso(curso);
        } else {
            alunoExistente.setCurso(null);
        }

        alunoRepository.save(alunoExistente);
    }

    public AlunoDTO buscarDTOporId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElse(null);
    }

    public boolean emailJaCadastrado(String email) {
        return alunoRepository.existsByEmail(email);
    }

    public boolean matriculaJaCadastrada(String matricula) {
        return alunoRepository.existsByMatricula(matricula);
    }

    public boolean emailJaCadastradoPorOutro(String email, Long id) {
        return alunoRepository.existsByEmailAndIdNot(email, id);
    }

    public boolean matriculaJaCadastradaPorOutro(String matricula, Long id) {
        return alunoRepository.existsByMatriculaAndIdNot(matricula, id);
    }

    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }

    private AlunoDTO converterParaDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setIdade(aluno.getIdade());
        dto.setEmail(aluno.getEmail());
        dto.setMatricula(aluno.getMatricula());

        if (aluno.getCurso() != null) {
            dto.setCursoId(aluno.getCurso().getId());
            dto.setCursoNome(aluno.getCurso().getNome());
        } else {
            dto.setCursoNome("Não informado");
        }

        return dto;
    }

    private Aluno converterParaEntidade(AlunoDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setId(dto.getId());
        aluno.setNome(dto.getNome());
        aluno.setIdade(dto.getIdade());
        aluno.setEmail(dto.getEmail());
        aluno.setMatricula(dto.getMatricula());

        if (dto.getCursoId() != null) {
            Curso curso = cursoRepository.findById(dto.getCursoId()).orElse(null);
            aluno.setCurso(curso);
        }
        return aluno;
    }
}