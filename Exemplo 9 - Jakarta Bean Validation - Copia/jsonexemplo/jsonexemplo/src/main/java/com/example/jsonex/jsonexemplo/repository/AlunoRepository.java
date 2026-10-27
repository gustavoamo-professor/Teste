package com.example.jsonex.jsonexemplo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Aluno;
public interface AlunoRepository
        extends JpaRepository<Aluno, Long> {

    // Pesquisa por parte do nome
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    // Verifica se já existe o e-mail
    boolean existsByEmailIgnoreCase(String email);
    // Verifica se já existe a matrícula
    boolean existsByMatricula(String matricula);
    // Usados durante a edição:
    // verifica se outro aluno possui esse e-mail
    boolean existsByEmailIgnoreCaseAndIdNot(
        String email,
        Long id
    );

    // verifica se outro aluno possui essa matrícula
    boolean existsByMatriculaAndIdNot(
        String matricula,
        Long id
    );
}
