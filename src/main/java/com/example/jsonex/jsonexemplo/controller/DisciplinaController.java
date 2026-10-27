package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;

import jakarta.validation.Valid;

@Controller
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "disciplinas";
    }

    @GetMapping("/disciplinas/novo")
    public String novaDisciplina(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        model.addAttribute("titulo", "Cadastrar Disciplina");
        return "formulario-disciplina";
    }

    @PostMapping("/disciplinas")
    public String cadastrarDisciplina(
            @Valid @ModelAttribute("disciplina") Disciplina disciplina,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (disciplinaService.nomeJaCadastrado(disciplina.getNome())) {
            resultado.rejectValue("nome", "nome.duplicado", "Já existe uma disciplina com esse nome");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Cadastrar Disciplina");
            return "formulario-disciplina";
        }

        disciplinaService.cadastrar(disciplina);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina cadastrada com sucesso.");
        return "redirect:/disciplinas";
    }

    @GetMapping("/disciplinas/{id}/editar")
    public String editarDisciplina(@PathVariable Long id, Model model) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);
        if (disciplina == null) {
            return "redirect:/disciplinas";
        }
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("titulo", "Editar Disciplina");
        return "formulario-disciplina";
    }

    @PostMapping("/disciplinas/{id}/editar")
    public String atualizarDisciplina(
            @PathVariable Long id,
            @Valid @ModelAttribute("disciplina") Disciplina disciplina,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        disciplina.setId(id);

        if (disciplinaService.nomeJaCadastradoPorOutra(disciplina.getNome(), id)) {
            resultado.rejectValue("nome", "nome.duplicado", "Já existe outra disciplina com esse nome");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Editar Disciplina");
            return "formulario-disciplina";
        }

        disciplinaService.atualizar(id, disciplina);
        redirectAttributes.addFlashAttribute("sucesso", "Disciplina atualizada com sucesso.");
        return "redirect:/disciplinas";
    }

    @PostMapping("/disciplinas/{id}/excluir")
    public String excluirDisciplina(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (disciplinaService.possuiMatriculas(id)) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível excluir a disciplina, pois existem alunos matriculados nela.");
            return "redirect:/disciplinas";
        }

        boolean excluiu = disciplinaService.excluir(id);
        if (excluiu) {
            redirectAttributes.addFlashAttribute("sucesso", "Disciplina excluída com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Disciplina não encontrada.");
        }
        return "redirect:/disciplinas";
    }
}