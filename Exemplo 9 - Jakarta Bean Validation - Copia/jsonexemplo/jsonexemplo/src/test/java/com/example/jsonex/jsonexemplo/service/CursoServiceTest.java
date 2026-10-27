package com.example.jsonex.jsonexemplo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.jsonex.jsonexemplo.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        cursoService = new CursoService(cursoRepository);
    }

    @Test
    void deveDetectarNomeDuplicado() {
        when(cursoRepository.existsByNomeIgnoreCase("Java")).thenReturn(true);

        assertTrue(cursoService.nomeJaCadastrado("Java"));
    }

    @Test
    void deveNormalizarNomeAoSalvar() {
        when(cursoRepository.existsByNomeIgnoreCase("Java")).thenReturn(false);

        assertEquals(false, cursoService.nomeJaCadastrado("Java"));
    }
}
