package com.example.jsonex.jsonexemplo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.jsonex.jsonexemplo.dto.AlunoDTO;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        alunoService = new AlunoService(alunoRepository, cursoRepository);
    }

    @Test
    void deveReaproveitarCursoExistenteAoCadastrarAluno() {
        Curso cursoExistente = new Curso();
        cursoExistente.setId(10L);
        cursoExistente.setNome("Java");

when(cursoRepository.findByNomeIgnoreCaseOrderByIdAsc("Java")).thenReturn(Arrays.asList(cursoExistente)); 

        AlunoDTO dto = new AlunoDTO();
        dto.setNome("Ana");
        dto.setIdade(20);
        dto.setCurso("Java");
        dto.setEmail("ana@email.com");
        dto.setMatricula("M-001");

        AlunoDTO salvo = alunoService.cadastrar(dto);

        assertEquals("Java", salvo.getCurso());
        verify(cursoRepository, never()).save(any(Curso.class));
    }
}
