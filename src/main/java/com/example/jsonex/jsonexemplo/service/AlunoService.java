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

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public List<Aluno> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }
        return alunoRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }
}