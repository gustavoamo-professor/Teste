package com.example.jsonex.jsonexemplo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jsonex.jsonexemplo.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findByCursoId(Long cursoId, Sort sort);

    boolean existsByCursoId(Long cursoId);

    // Nome único dentro do mesmo curso
    boolean existsByNomeIgnoreCaseAndCursoId(String nome, Long cursoId);

    // Usado durante a edição
    boolean existsByNomeIgnoreCaseAndCursoIdAndIdNot(
            String nome,
            Long cursoId,
            Long id);
}
