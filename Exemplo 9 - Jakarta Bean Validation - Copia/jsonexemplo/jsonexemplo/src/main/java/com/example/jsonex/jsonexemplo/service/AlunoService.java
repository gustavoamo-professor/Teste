
package com.example.jsonex.jsonexemplo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.dto.AlunoDTO;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    // ==========================================
    // LISTAR TODOS
    // ==========================================
    public List<AlunoDTO> listarTodos() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoDTO> alunosDTO = new ArrayList<>();

        for (Aluno aluno : alunos) {
            alunosDTO.add(paraDTO(aluno));
        }

        return alunosDTO;
    }

    // ==========================================
    // PESQUISAR POR NOME
    // ==========================================
    public List<AlunoDTO> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome.trim());
        List<AlunoDTO> alunosDTO = new ArrayList<>();

        for (Aluno aluno : alunos) {
            alunosDTO.add(paraDTO(aluno));
        }

        return alunosDTO;
    }

    // ==========================================
    // BUSCAR POR ID
    // ==========================================
    public AlunoDTO buscarDTOporId(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);

        if (aluno == null) {
            return null;
        }

        return paraDTO(aluno);
    }

    // ==========================================
    // VERIFICAR EMAIL NO CADASTRO
    // ==========================================
    public boolean emailJaCadastrado(String email) {
        if (email == null) {
            return false;
        }

        return alunoRepository.existsByEmailIgnoreCase(email.trim());
    }

    // ==========================================
    // VERIFICAR MATRÍCULA NO CADASTRO
    // ==========================================
    public boolean matriculaJaCadastrada(String matricula) {
        if (matricula == null) {
            return false;
        }

        return alunoRepository.existsByMatricula(matricula.trim());
    }

    // ==========================================
    // VERIFICAR EMAIL DURANTE A EDIÇÃO
    // ==========================================
    public boolean emailJaCadastradoPorOutro(String email, Long id) {
        if (email == null) {
            return false;
        }

        return alunoRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), id);
    }

    // ==========================================
    // VERIFICAR MATRÍCULA DURANTE A EDIÇÃO
    // ==========================================
    public boolean matriculaJaCadastradaPorOutro(String matricula, Long id) {
        if (matricula == null) {
            return false;
        }

        return alunoRepository.existsByMatriculaAndIdNot(matricula.trim(), id);
    }

    // ==========================================
    // CADASTRAR
    // ==========================================
    public AlunoDTO cadastrar(AlunoDTO dto) {
        if (dto == null || dto.getCurso() == null || dto.getCurso().isBlank()) {
            return null;
        }

        Curso curso = buscarOuCriarCurso(dto.getCurso());
        if (curso == null) {
            return null;
        }

        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setIdade(dto.getIdade());
        aluno.setEmail(dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : null);
        aluno.setMatricula(dto.getMatricula() != null ? dto.getMatricula().trim() : null);
        aluno.setCurso(curso);

        Aluno alunoSalvo = alunoRepository.save(aluno);
        return paraDTO(alunoSalvo);
    }

    // ==========================================
    // ATUALIZAR
    // ==========================================
    public AlunoDTO atualizar(Long id, AlunoDTO dto) {
        Aluno alunoExistente = alunoRepository.findById(id).orElse(null);

        if (alunoExistente == null) {
            return null;
        }

        alunoExistente.setNome(dto.getNome());
        alunoExistente.setIdade(dto.getIdade());
        alunoExistente.setEmail(dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : null);
        alunoExistente.setMatricula(dto.getMatricula() != null ? dto.getMatricula().trim() : null);

        if (dto.getCurso() != null && !dto.getCurso().isBlank()) {
            Curso curso = buscarOuCriarCurso(dto.getCurso());
            if (curso != null) {
                alunoExistente.setCurso(curso);
            }
        }

        Aluno alunoAtualizado = alunoRepository.save(alunoExistente);
        return paraDTO(alunoAtualizado);
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    public boolean excluir(Long id) {
        if (!alunoRepository.existsById(id)) {
            return false;
        }

        alunoRepository.deleteById(id);
        return true;
    }

    private Curso buscarOuCriarCurso(String nomeCurso) {
    if (nomeCurso == null || nomeCurso.isBlank()) {
        return null;
    }

    String nomeNormalizado = nomeCurso.trim();
    List<Curso> cursosExistentes = cursoRepository.findByNomeIgnoreCaseOrderByIdAsc(nomeNormalizado);

    if (!cursosExistentes.isEmpty()) {
        return cursosExistentes.get(0); // Retorna o primeiro encontrado e ignora eventuais duplicatas
    }

    Curso novoCurso = new Curso(nomeNormalizado);
    return cursoRepository.save(novoCurso);
}

    // ==========================================
    // CONVERTER ALUNO → DTO
    // ==========================================
    private AlunoDTO paraDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setIdade(aluno.getIdade());
        dto.setCurso(aluno.getCurso() != null ? aluno.getCurso().getNome() : null);
        dto.setEmail(aluno.getEmail());
        dto.setMatricula(aluno.getMatricula());
        return dto;
    }
}

