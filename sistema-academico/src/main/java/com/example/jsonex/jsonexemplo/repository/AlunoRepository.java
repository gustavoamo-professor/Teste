package com.example.jsonex.jsonexemplo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jsonex.jsonexemplo.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findByNomeContainingIgnoreCase(String nome, Sort sort);

    List<Aluno> findByCursoId(Long cursoId, Sort sort);

    boolean existsByCursoId(Long cursoId);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByRaIgnoreCase(String ra);

    boolean existsByRaIgnoreCaseAndIdNot(String ra, Long id);
}
