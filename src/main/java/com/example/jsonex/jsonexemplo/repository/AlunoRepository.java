package com.example.jsonex.jsonexemplo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jsonex.jsonexemplo.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}