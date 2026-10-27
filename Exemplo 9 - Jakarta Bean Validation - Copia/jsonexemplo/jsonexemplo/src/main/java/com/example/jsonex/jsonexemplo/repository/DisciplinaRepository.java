package com.example.jsonex.jsonexemplo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jsonex.jsonexemplo.model.Disciplina;

public interface DisciplinaRepository
        extends JpaRepository<Disciplina, Long> {

    // Verifica se já existe disciplina com esse nome
    boolean existsByNomeIgnoreCase(String nome);

    // Usado durante a edição
    boolean existsByNomeIgnoreCaseAndIdNot(
            String nome,
            Long id
    );
}
