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

import com.example.jsonex.jsonexemplo.dto.MatriculaForm;
import com.example.jsonex.jsonexemplo.exception.RegraNegocioException;
import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Matricula;
import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;
import com.example.jsonex.jsonexemplo.service.MatriculaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/matriculas")
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

    // ==========================================
    // LISTAR (todas ou só as de um aluno)
    // ==========================================
    @GetMapping
    public String listar(
            @RequestParam(required = false) Long alunoId,
            Model model) {

        Aluno alunoFiltro = alunoId != null
                ? alunoService.buscarPorId(alunoId) : null;

        if (alunoFiltro != null) {
            model.addAttribute("matriculas",
                    matriculaService.listarPorAluno(alunoId));
        } else {
            model.addAttribute("matriculas", matriculaService.listarTodos());
        }
        model.addAttribute("alunoFiltro", alunoFiltro);
        return "matriculas";
    }

    // ==========================================
    // NOVA MATRÍCULA
    // ==========================================
    @GetMapping("/nova")
    public String nova(
            @RequestParam(required = false) Long alunoId,
            Model model) {

        MatriculaForm form = new MatriculaForm();
        form.setAlunoId(alunoId);
        prepararFormulario(model, form, "Nova Matrícula", "/matriculas", false);
        return "formulario-matricula";
    }

    @PostMapping
    public String matricular(
            @Valid @ModelAttribute("matriculaForm") MatriculaForm form,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!resultado.hasErrors()) {
            try {
                matriculaService.matricular(
                        form.getAlunoId(),
                        form.getDisciplinaId(),
                        form.getNota(),
                        form.getFrequencia());
            } catch (RegraNegocioException e) {
                resultado.reject("regra.negocio", e.getMessage());
            }
        }

        if (resultado.hasErrors()) {
            prepararFormulario(model, form,
                    "Nova Matrícula", "/matriculas", false);
            return "formulario-matricula";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Matrícula realizada com sucesso.");
        return "redirect:/matriculas?alunoId=" + form.getAlunoId();
    }

    // ==========================================
    // EDITAR (nota e frequência)
    // ==========================================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Matricula matricula = matriculaService.buscarPorId(id);
        if (matricula == null) {
            return "redirect:/matriculas";
        }

        MatriculaForm form = new MatriculaForm();
        form.setId(matricula.getId());
        form.setAlunoId(matricula.getAluno().getId());
        form.setDisciplinaId(matricula.getDisciplina().getId());
        form.setNota(matricula.getNota());
        form.setFrequencia(matricula.getFrequencia());

        prepararFormulario(model, form, "Editar Matrícula",
                "/matriculas/" + id + "/editar", true);
        adicionarResumo(model, matricula);
        return "formulario-matricula";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("matriculaForm") MatriculaForm form,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        Matricula matricula = matriculaService.buscarPorId(id);
        if (matricula == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Matrícula não encontrada.");
            return "redirect:/matriculas";
        }

        // Aluno e disciplina não mudam na edição: o que vale é o banco,
        // não o que veio do formulário.
        form.setId(id);
        form.setAlunoId(matricula.getAluno().getId());
        form.setDisciplinaId(matricula.getDisciplina().getId());

        // Aluno e disciplina são fixos na edição; só nota e frequência
        // precisam ser validadas.
        if (resultado.hasFieldErrors("nota")
                || resultado.hasFieldErrors("frequencia")) {
            prepararFormulario(model, form, "Editar Matrícula",
                    "/matriculas/" + id + "/editar", true);
            adicionarResumo(model, matricula);
            return "formulario-matricula";
        }

        matriculaService.atualizar(id, form.getNota(), form.getFrequencia());

        redirectAttributes.addFlashAttribute(
                "sucesso", "Matrícula atualizada com sucesso.");
        return "redirect:/matriculas?alunoId=" + matricula.getAluno().getId();
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Matricula matricula = matriculaService.buscarPorId(id);

        if (matriculaService.excluir(id)) {
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Matrícula excluída com sucesso.");
            return "redirect:/matriculas?alunoId="
                    + matricula.getAluno().getId();
        }

        redirectAttributes.addFlashAttribute(
                "erro", "Matrícula não encontrada.");
        return "redirect:/matriculas";
    }

    // ==========================================
    // AUXILIARES
    // ==========================================
    private void prepararFormulario(
            Model model,
            MatriculaForm form,
            String titulo,
            String acao,
            boolean edicao) {

        model.addAttribute("matriculaForm", form);
        model.addAttribute("titulo", titulo);
        model.addAttribute("acao", acao);
        model.addAttribute("edicao", edicao);

        if (!edicao) {
            model.addAttribute("alunos", alunoService.listarTodos());
            model.addAttribute("disciplinas", disciplinaService.listarTodos());
        }
    }

    private void adicionarResumo(Model model, Matricula matricula) {
        model.addAttribute("alunoNome", matricula.getAluno().getNome());
        model.addAttribute("disciplinaNome",
                matricula.getDisciplina().getNome());
    }
}
