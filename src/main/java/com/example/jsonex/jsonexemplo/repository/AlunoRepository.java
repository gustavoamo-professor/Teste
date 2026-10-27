package com.example.jsonex.jsonexemplo.repository;

import com.example.jsonex.jsonexemplo.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // Método de pesquisa por nome que você já deve ter:
    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    // Métodos simples de verificação:
    boolean existsByEmail(String email);
    boolean existsByMatricula(String matricula);

    // ADICIONE ESTAS DUAS LINHAS ABAIXO PARA CORRIGIR O ERRO:
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);
}
