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
    // LISTAR TODOS
    // ==========================================

    public List<Aluno> listarTodos() {

        return alunoRepository.findAll();
    }


    // ==========================================
    // PESQUISAR POR NOME
    // ==========================================

    public List<Aluno> pesquisarPorNome(String nome) {


        // Se a pesquisa estiver vazia,
        // retorna todos os alunos.
        if (nome == null || nome.isBlank()) {

            return listarTodos();
        }


        // Remove espaços desnecessários
        // no início e no final.
        nome = nome.trim();


        // Pesquisa pelo nome ignorando
        // maiúsculas e minúsculas.
        return alunoRepository
                .findByNomeContainingIgnoreCase(nome);
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
    // CADASTRAR
    // ==========================================

    public Aluno cadastrar(Aluno aluno) {

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
                alunoAtualizado.getNome());

        alunoExistente.setIdade(
                alunoAtualizado.getIdade());

        alunoExistente.setCurso(
                alunoAtualizado.getCurso());

        alunoExistente.setEmail(
                alunoAtualizado.getEmail());

        alunoExistente.setMatricula(
                alunoAtualizado.getMatricula());


        return alunoRepository.save(
                alunoExistente);
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