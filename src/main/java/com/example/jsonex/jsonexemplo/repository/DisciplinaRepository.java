package com.example.jsonex.jsonexemplo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}