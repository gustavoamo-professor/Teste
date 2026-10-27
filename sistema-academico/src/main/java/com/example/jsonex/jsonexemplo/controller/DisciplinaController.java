package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.service.CursoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;
    private final CursoService cursoService;

    public DisciplinaController(
            DisciplinaService disciplinaService,
            CursoService cursoService) {
        this.disciplinaService = disciplinaService;
        this.cursoService = cursoService;
    }

    // ==========================================
    // LISTAR
    // ==========================================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
        return "disciplinas";
    }

    // ==========================================
    // CADASTRAR
    // ==========================================
    @GetMapping("/novo")
    public String nova(Model model) {
        prepararFormulario(model, new Disciplina(),
                "Cadastrar Disciplina", "/disciplinas");
        return "formulario-disciplina";
    }

    @PostMapping
    public String cadastrar(
            @Valid @ModelAttribute("disciplina") Disciplina disciplina,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        validarRegras(disciplina, null, resultado,
                "Já existe uma disciplina com esse nome neste curso");

        if (resultado.hasErrors()) {
            prepararFormulario(model, disciplina,
                    "Cadastrar Disciplina", "/disciplinas");
            return "formulario-disciplina";
        }

        try {
            disciplinaService.cadastrar(disciplina);
        } catch (RegraNegocioException e) {
            resultado.reject("regra.negocio", e.getMessage());
            prepararFormulario(model, disciplina,
                    "Cadastrar Disciplina", "/disciplinas");
            return "formulario-disciplina";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Disciplina cadastrada com sucesso.");
        return "redirect:/disciplinas";
    }

    // ==========================================
    // EDITAR
    // ==========================================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Disciplina disciplina = disciplinaService.buscarPorId(id);
        if (disciplina == null) {
            return "redirect:/disciplinas";
        }
        prepararFormulario(model, disciplina, "Editar Disciplina",
                "/disciplinas/" + id + "/editar");
        return "formulario-disciplina";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("disciplina") Disciplina disciplina,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        disciplina.setId(id);
        validarRegras(disciplina, id, resultado,
                "Já existe outra disciplina com esse nome neste curso");

        String acao = "/disciplinas/" + id + "/editar";

        if (resultado.hasErrors()) {
            prepararFormulario(model, disciplina, "Editar Disciplina", acao);
            return "formulario-disciplina";
        }

        try {
            if (disciplinaService.atualizar(id, disciplina) == null) {
                redirectAttributes.addFlashAttribute(
                        "erro", "Disciplina não encontrada.");
                return "redirect:/disciplinas";
            }
        } catch (RegraNegocioException e) {
            resultado.reject("regra.negocio", e.getMessage());
            prepararFormulario(model, disciplina, "Editar Disciplina", acao);
            return "formulario-disciplina";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Disciplina atualizada com sucesso.");
        return "redirect:/disciplinas";
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        // Verifica se existem matrículas
        if (disciplinaService.possuiMatriculas(id)) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não é possível excluir a disciplina, pois existem "
                    + "alunos matriculados nela.");
        } else if (disciplinaService.excluir(id)) {
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Disciplina excluída com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "erro", "Disciplina não encontrada.");
        }
        return "redirect:/disciplinas";
    }

    // ==========================================
    // AUXILIARES
    // ==========================================
    private void validarRegras(
            Disciplina disciplina,
            Long idIgnorar,
            BindingResult resultado,
            String mensagemDuplicada) {

        Long cursoId = disciplina.getCurso() != null
                ? disciplina.getCurso().getId() : null;

        if (cursoId == null) {
            resultado.rejectValue("curso", "curso.obrigatorio",
                    "Selecione um curso");
        } else if (disciplinaService.nomeEmUso(
                disciplina.getNome(), cursoId, idIgnorar)) {
            resultado.rejectValue("nome", "nome.duplicado",
                    mensagemDuplicada);
        }
    }

    private void prepararFormulario(
            Model model, Disciplina disciplina, String titulo, String acao) {
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("titulo", titulo);
        model.addAttribute("acao", acao);
        model.addAttribute("cursos", cursoService.listarTodos());
    }
}
