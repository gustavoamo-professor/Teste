package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.service.CursoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;
import jakarta.validation.Valid;

@Controller
public class DisciplinaController {
    private final DisciplinaService disciplinaService;
    private final CursoService cursoService;
    public DisciplinaController(DisciplinaService disciplinaService, CursoService cursoService) {
        this.disciplinaService = disciplinaService;
        this.cursoService = cursoService;
    }
    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "disciplinas";
    }
    @GetMapping("/disciplinas/novo")
    public String novaDisciplina(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("titulo", "Cadastrar Disciplina");
        return "formulario-disciplina";
    }
    @PostMapping("/disciplinas")
    public String cadastrarDisciplina(@Valid @ModelAttribute("disciplina") Disciplina disciplina, BindingResult resultado,
            @RequestParam(name = "cursoId", required = false) Long cursoId, Model model, RedirectAttributes redirectAttributes) {
        Curso curso = cursoId != null
                ? cursoService.listarTodos().stream()
                        .filter(c -> c.getId() != null && c.getId().equals(cursoId))
                        .findFirst()
                        .orElse(null)
                : null;
        if (curso == null) resultado.reject("curso.obrigatorio", "Selecione um curso válido");
        if (disciplinaService.nomeJaCadastrado(disciplina.getNome())) resultado.rejectValue("nome", "nome.duplicado", "Já existe uma disciplina com esse nome");
        if (resultado.hasErrors()) {
            model.addAttribute("cursos", cursoService.listarTodos());
            model.addAttribute("titulo", "Cadastrar Disciplina");
            return "formulario-disciplina";
        }
        disciplina.setCurso(curso);
        disciplinaService.cadastrar(disciplina);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina cadastrada com sucesso.");
        return "redirect:/disciplinas";
    }
}