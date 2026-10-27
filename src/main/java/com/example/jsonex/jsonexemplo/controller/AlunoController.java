package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.jsonex.jsonexemplo.service.AlunoService;

@Controller
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/alunos";
    }

    @GetMapping("/alunos")
    public String listarAlunos(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "aluno";
    }
}