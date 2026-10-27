package com.example.jsonex.jsonexemplo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByAlunoId(Long alunoId);
    List<Matricula> findByDisciplinaId(Long disciplinaId);
    boolean existsByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);
    boolean existsByAlunoId(Long alunoId);
    boolean existsByDisciplinaId(Long disciplinaId);
}