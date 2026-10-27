package com.example.jsonex.jsonexemplo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jsonex.jsonexemplo.dto.CursoDTO;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<CursoDTO> listarTodos() {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoDTO> cursosDTO = new ArrayList<>();

        for (Curso curso : cursos) {
            cursosDTO.add(paraDTO(curso));
        }

        return cursosDTO;
    }

    public List<CursoDTO> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        List<Curso> cursos = cursoRepository.findByNomeContainingIgnoreCase(nome.trim());
        List<CursoDTO> cursosDTO = new ArrayList<>();

        for (Curso curso : cursos) {
            cursosDTO.add(paraDTO(curso));
        }

        return cursosDTO;
    }

    public CursoDTO buscarDTOPorId(Long id) {
        Curso curso = cursoRepository.findById(id).orElse(null);

        if (curso == null) {
            return null;
        }

        return paraDTO(curso);
    }

    public boolean nomeJaCadastrado(String nome) {
        if (nome == null) {
            return false;
        }

        return cursoRepository.existsByNomeIgnoreCase(nome.trim());
    }

    public boolean nomeJaCadastradoPorOutro(String nome, Long id) {
        if (nome == null) {
            return false;
        }

        return cursoRepository.existsByNomeIgnoreCaseAndIdNot(nome.trim(), id);
    }

    public CursoDTO cadastrar(CursoDTO dto) {
        if (dto == null || dto.getNome() == null || dto.getNome().isBlank()) {
            return null;
        }

        Curso curso = new Curso();
        curso.setNome(dto.getNome().trim());

        Curso salvo = cursoRepository.save(curso);
        return paraDTO(salvo);
    }

  
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public CursoDTO atualizar(Long id, CursoDTO dto) {
        Curso cursoExistente = cursoRepository.findById(id).orElse(null);

        if (cursoExistente == null) {
            return null;
        }

        cursoExistente.setNome(dto.getNome().trim());
        Curso atualizado = cursoRepository.save(cursoExistente);
        return paraDTO(atualizado);
    }

    public boolean excluir(Long id) {
        if (!cursoRepository.existsById(id)) {
            return false;
        }

        cursoRepository.deleteById(id);
        return true;
    }

    private CursoDTO paraDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
        return dto;
    }
}
