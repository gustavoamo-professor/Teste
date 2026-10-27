package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.CursoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final CursoService cursoService;

    public AlunoController(
            AlunoService alunoService,
            CursoService cursoService) {
        this.alunoService = alunoService;
        this.cursoService = cursoService;
    }

    // ==========================================
    // LISTAR / PESQUISAR
    // ==========================================
    @GetMapping
    public String listar(
            @RequestParam(required = false) String nome,
            Model model) {
        model.addAttribute("alunos", alunoService.pesquisarPorNome(nome));
        model.addAttribute("nomePesquisa", nome);
        return "alunos";
    }

    // ==========================================
    // CADASTRAR
    // ==========================================
    @GetMapping("/novo")
    public String novo(Model model) {
        prepararFormulario(model, new Aluno(), "Cadastrar Aluno", "/alunos");
        return "formulario-aluno";
    }

    @PostMapping
    public String cadastrar(
            @Valid @ModelAttribute("aluno") Aluno aluno,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        validarRegras(aluno, null, resultado);

        if (resultado.hasErrors()) {
            prepararFormulario(model, aluno, "Cadastrar Aluno", "/alunos");
            return "formulario-aluno";
        }

        try {
            alunoService.cadastrar(aluno);
        } catch (RegraNegocioException e) {
            resultado.reject("regra.negocio", e.getMessage());
            prepararFormulario(model, aluno, "Cadastrar Aluno", "/alunos");
            return "formulario-aluno";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Aluno cadastrado com sucesso.");
        return "redirect:/alunos";
    }

    // ==========================================
    // EDITAR
    // ==========================================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = alunoService.buscarPorId(id);
        if (aluno == null) {
            return "redirect:/alunos";
        }
        prepararFormulario(model, aluno, "Editar Aluno",
                "/alunos/" + id + "/editar");
        return "formulario-aluno";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("aluno") Aluno aluno,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        aluno.setId(id);
        validarRegras(aluno, id, resultado);

        String acao = "/alunos/" + id + "/editar";

        if (resultado.hasErrors()) {
            prepararFormulario(model, aluno, "Editar Aluno", acao);
            return "formulario-aluno";
        }

        try {
            if (alunoService.atualizar(id, aluno) == null) {
                redirectAttributes.addFlashAttribute(
                        "erro", "Aluno não encontrado.");
                return "redirect:/alunos";
            }
        } catch (RegraNegocioException e) {
            resultado.reject("regra.negocio", e.getMessage());
            prepararFormulario(model, aluno, "Editar Aluno", acao);
            return "formulario-aluno";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Aluno atualizado com sucesso.");
        return "redirect:/alunos";
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (alunoService.possuiMatriculas(id)) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não é possível excluir o aluno, pois ele possui "
                    + "matrículas. Exclua as matrículas antes.");
        } else if (alunoService.excluir(id)) {
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Aluno excluído com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "erro", "Aluno não encontrado.");
        }
        return "redirect:/alunos";
    }

    // ==========================================
    // AUXILIARES
    // ==========================================
    private void validarRegras(
            Aluno aluno, Long idIgnorar, BindingResult resultado) {

        if (aluno.getCurso() == null || aluno.getCurso().getId() == null) {
            resultado.rejectValue("curso", "curso.obrigatorio",
                    "Selecione um curso");
        }
        if (alunoService.emailEmUso(aluno.getEmail(), idIgnorar)) {
            resultado.rejectValue("email", "email.duplicado",
                    "Já existe um aluno com esse e-mail");
        }
        if (alunoService.raEmUso(aluno.getRa(), idIgnorar)) {
            resultado.rejectValue("ra", "ra.duplicado",
                    "Já existe um aluno com esse RA");
        }
    }

    private void prepararFormulario(
            Model model, Aluno aluno, String titulo, String acao) {
        model.addAttribute("aluno", aluno);
        model.addAttribute("titulo", titulo);
        model.addAttribute("acao", acao);
        model.addAttribute("cursos", cursoService.listarTodos());
    }
}
