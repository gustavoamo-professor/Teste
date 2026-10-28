package com.example.jsonex.jsonexemplo.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    // ==========================================
    // LISTAR
    // ==========================================

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    // ==========================================
    // PESQUISAR
    // ==========================================

    public List<Aluno> pesquisarPorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        return alunoRepository
                .findByNomeContainingIgnoreCase(
                    nome.trim()
                );
    }

    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Aluno buscarPorId(Long id) {

        return alunoRepository
                .findById(id)
                .orElse(null);
    }

    // ==========================================
    // VERIFICAR E-MAIL NO CADASTRO
    // ==========================================

    public boolean emailJaCadastrado(String email) {

        if (email == null) {
            return false;
        }

        return alunoRepository
                .existsByEmailIgnoreCase(
                    email.trim()
                );
    }

    // ==========================================
    // VERIFICAR MATRÍCULA NO CADASTRO
    // ==========================================

    public boolean matriculaJaCadastrada(
            String matricula) {

        if (matricula == null) {
            return false;
        }

        return alunoRepository
                .existsByMatricula(
                    matricula.trim()
                );
    }

    // ==========================================
    // VERIFICAR E-MAIL NA EDIÇÃO
    // ==========================================

    public boolean emailJaCadastradoPorOutro(
            String email,
            Long id) {

        if (email == null) {
            return false;
        }

        return alunoRepository
                .existsByEmailIgnoreCaseAndIdNot(
                    email.trim(),
                    id
                );
    }

    // ==========================================
    // VERIFICAR MATRÍCULA NA EDIÇÃO
    // ==========================================

    public boolean matriculaJaCadastradaPorOutro(
            String matricula,
            Long id) {

        if (matricula == null) {
            return false;
        }

        return alunoRepository
                .existsByMatriculaAndIdNot(
                    matricula.trim(),
                    id
                );
    }

    // ==========================================
    // CADASTRAR
    // ==========================================

    public Aluno cadastrar(Aluno aluno) {

        // Normaliza os dados antes de salvar
        aluno.setEmail(
            aluno.getEmail().trim().toLowerCase()
        );

        aluno.setMatricula(
            aluno.getMatricula().trim()
        );

        return alunoRepository.save(aluno);
    }

    // ==========================================
    // ATUALIZAR
    // ==========================================

    public Aluno atualizar(
            Long id,
            Aluno alunoAtualizado) {

        Aluno alunoExistente =
            buscarPorId(id);

        if (alunoExistente == null) {
            return null;
        }

        alunoExistente.setNome(
            alunoAtualizado.getNome()
        );

        alunoExistente.setIdade(
            alunoAtualizado.getIdade()
        );

        alunoExistente.setCurso(
            alunoAtualizado.getCurso()
        );

        alunoExistente.setEmail(
            alunoAtualizado
                .getEmail()
                .trim()
                .toLowerCase()
        );

        alunoExistente.setMatricula(
            alunoAtualizado
                .getMatricula()
                .trim()
        );

        return alunoRepository.save(
            alunoExistente
        );
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
}

