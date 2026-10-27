package com.example.jsonex.jsonexemplo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}