package com.example.jsonex.jsonexemplo.service;

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

    // Retorna a lista de entidades Curso para o listarTodos()
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    // Busca o curso por ID e o converte diretamente para CursoDTO
    public CursoDTO buscarPorIdDTO(Long id) {
        Curso curso = cursoRepository.findById(id).orElse(null);
        if (curso == null) {
            return null;
        }
        return new CursoDTO(curso.getId(), curso.getNome());
    }

    // Método para cadastrar um novo curso a partir do DTO
    public void cadastrar(CursoDTO dto) {
        Curso curso = new Curso(dto.getNome());
        cursoRepository.save(curso);
    }

    // Método para atualizar um curso existente
    public void atualizar(Long id, CursoDTO dto) {
        Curso curso = cursoRepository.findById(id).orElse(null);
        if (curso != null) {
            curso.setNome(dto.getNome());
            cursoRepository.save(curso);
        }
    }

    // Método para excluir um curso do banco
    public void excluir(Long id) {
        cursoRepository.deleteById(id);
    }
}
