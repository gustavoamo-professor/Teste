package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;
import com.example.jsonex.jsonexemplo.service.MatriculaService;

@Controller
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;

    public MatriculaController(
            MatriculaService matriculaService,
            AlunoService alunoService,
            DisciplinaService disciplinaService) {
        this.matriculaService = matriculaService;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
    }

    @GetMapping("/matriculas")
    public String listar(
            @RequestParam(required = false) Long alunoId,
            Model model) {

        if (alunoId != null) {
            model.addAttribute("matriculas", matriculaService.listarPorAluno(alunoId));
            model.addAttribute("alunoSelecionadoId", alunoId);
        } else {
            model.addAttribute("matriculas", matriculaService.listarTodos());
        }

        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "matriculas";
    }

    @PostMapping("/matriculas")
    public String matricular(
            @RequestParam Long alunoId,
            @RequestParam Long disciplinaId,
            RedirectAttributes redirectAttributes) {

        try {
            matriculaService.matricular(alunoId, disciplinaId);
            redirectAttributes.addFlashAttribute("sucesso", "Matrícula realizada com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/matriculas" + (alunoId != null ? "?alunoId=" + alunoId : "");
    }

    @PostMapping("/matriculas/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            @RequestParam(required = false) Long alunoId,
            RedirectAttributes redirectAttributes) {

        boolean excluiu = matriculaService.excluir(id);
        if (excluiu) {
            redirectAttributes.addFlashAttribute("sucesso", "Matrícula excluída com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Matrícula não encontrada.");
        }

        return "redirect:/matriculas" + (alunoId != null ? "?alunoId=" + alunoId : "");
    }
}