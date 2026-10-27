package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jsonex.jsonexemplo.dto.CursoDTO;
import com.example.jsonex.jsonexemplo.service.CursoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursoController { // Nome corrigido aqui

    private final CursoService cursoService;

    // O construtor precisa ter o mesmo nome da classe
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos";
    }

    @GetMapping("/novo")
    public String novoCurso(Model model) {
        model.addAttribute("curso", new CursoDTO());
        model.addAttribute("titulo", "Cadastrar Curso");
        return "formulario-curso";
    }

    @PostMapping
    public String salvarCurso(@Valid @ModelAttribute("curso") CursoDTO dto, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Cadastrar Curso");
            return "formulario-curso";
        }
        cursoService.cadastrar(dto);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    public String editarCurso(@PathVariable Long id, Model model) {
        CursoDTO dto = cursoService.buscarPorIdDTO(id);
        if (dto == null) {
            return "redirect:/cursos";
        }
        
        model.addAttribute("curso", dto);
        model.addAttribute("titulo", "Editar Curso");
        return "formulario-curso";
    }

    @PostMapping("/{id}/editar")
    public String atualizarCurso(@PathVariable Long id, @Valid @ModelAttribute("curso") CursoDTO dto, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Editar Curso");
            return "formulario-curso";
        }
        cursoService.atualizar(id, dto);
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/excluir")
    public String excluirCurso(@PathVariable Long id) {
        cursoService.excluir(id);
        return "redirect:/cursos";
    }
}
