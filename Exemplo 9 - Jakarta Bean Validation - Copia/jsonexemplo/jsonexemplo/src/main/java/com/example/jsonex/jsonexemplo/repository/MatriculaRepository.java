package com.example.jsonex.jsonexemplo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Matricula;

public interface MatriculaRepository
        extends JpaRepository<Matricula, Long> {

    // Matrículas de determinado aluno
    List<Matricula> findByAlunoId(
            Long alunoId
    );

    // Matrículas de determinada disciplina
    List<Matricula> findByDisciplinaId(
            Long disciplinaId
    );

    // Cadastro:
    // verifica matrícula duplicada
    boolean existsByAlunoIdAndDisciplinaId(
            Long alunoId,
            Long disciplinaId
    );

    // Edição:
    // verifica se outra matrícula já possui
    // a mesma combinação aluno + disciplina
    boolean existsByAlunoIdAndDisciplinaIdAndIdNot(
            Long alunoId,
            Long disciplinaId,
            Long id
    );

    // Utilizado para impedir exclusão de aluno
    boolean existsByAlunoId(
            Long alunoId
    );

    // Utilizado para impedir exclusão de disciplina
    boolean existsByDisciplinaId(
            Long disciplinaId
    );
}

