package com.example.jsonex.jsonexemplo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.repository.MatriculaRepository;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(
            DisciplinaRepository disciplinaRepository,
            MatriculaRepository matriculaRepository) {

        this.disciplinaRepository =
                disciplinaRepository;

        this.matriculaRepository =
                matriculaRepository;
    }

    // ==========================================
    // LISTAR
    // ==========================================

    public List<Disciplina> listarTodos() {

        return disciplinaRepository.findAll();
    }

    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Disciplina buscarPorId(Long id) {

        return disciplinaRepository
                .findById(id)
                .orElse(null);
    }

    // ==========================================
    // VERIFICAR NOME NO CADASTRO
    // ==========================================

    public boolean nomeJaCadastrado(String nome) {

        if (nome == null || nome.isBlank()) {
            return false;
        }

        return disciplinaRepository
                .existsByNomeIgnoreCase(
                        nome.trim()
                );
    }

    // ==========================================
    // VERIFICAR NOME NA EDIÇÃO
    // ==========================================

    public boolean nomeJaCadastradoPorOutra(
            String nome,
            Long id) {

        if (nome == null || nome.isBlank()) {
            return false;
        }

        return disciplinaRepository
                .existsByNomeIgnoreCaseAndIdNot(
                        nome.trim(),
                        id
                );
    }

    // ==========================================
    // CADASTRAR
    // ==========================================

    public Disciplina cadastrar(
            Disciplina disciplina) {

        disciplina.setNome(
                disciplina
                        .getNome()
                        .trim()
        );

        return disciplinaRepository.save(
                disciplina
        );
    }

    // ==========================================
    // ATUALIZAR
    // ==========================================
    public Disciplina atualizar(
            Long id,
            Disciplina disciplinaAtualizada) {

        Disciplina disciplinaExistente =
                buscarPorId(id);

        if (disciplinaExistente == null) {
            return null;
        }

        disciplinaExistente.setNome(
                disciplinaAtualizada
                        .getNome()
                        .trim()
        );

        disciplinaExistente.setCargaHoraria(
                disciplinaAtualizada
                        .getCargaHoraria()
        );

        // ADICIONE ESTA LINHA PARA SALVAR O CURSO CORRETAMENTE NA EDIÇÃO
        disciplinaExistente.setCurso(
                disciplinaAtualizada.getCurso()
        );

        return disciplinaRepository.save(
                disciplinaExistente
        );
    }

    // ==========================================
    // VERIFICAR SE POSSUI MATRÍCULAS
    // ==========================================

    public boolean possuiMatriculas(Long id) {

        return matriculaRepository
                .existsByDisciplinaId(id);
    }

    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluir(Long id) {

        if (!disciplinaRepository.existsById(id)) {
            return false;
        }

        // Não permite excluir disciplina
        // que possui matrículas.
        if (possuiMatriculas(id)) {
            return false;
        }

        disciplinaRepository.deleteById(id);

        return true;
    }
}
