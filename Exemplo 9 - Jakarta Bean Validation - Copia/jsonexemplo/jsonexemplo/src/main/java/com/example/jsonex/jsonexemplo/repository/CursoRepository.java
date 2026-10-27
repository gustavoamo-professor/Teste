package com.example.jsonex.jsonexemplo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jsonex.jsonexemplo.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByNomeContainingIgnoreCase(String nome);

    List<Curso> findByNomeIgnoreCaseOrderByIdAsc(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
