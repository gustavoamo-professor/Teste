package com.example.jsonex.jsonexemplo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.model.Matricula;
import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.CursoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;
import com.example.jsonex.jsonexemplo.service.MatriculaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;
    private final MatriculaService matriculaService;

    public CursoController(
            CursoService cursoService,
            AlunoService alunoService,
            DisciplinaService disciplinaService,
            MatriculaService matriculaService) {
        this.cursoService = cursoService;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
        this.matriculaService = matriculaService;
    }

    // ==========================================
    // LISTAR
    // ==========================================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos";
    }

    // ==========================================
    // DETALHE: Curso → Alunos → Matrículas → Disciplinas
    //                \-> Disciplinas
    // ==========================================
    @GetMapping("/{id}")
    public String detalhar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Curso curso = cursoService.buscarPorId(id);
        if (curso == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Curso não encontrado.");
            return "redirect:/cursos";
        }

        List<Aluno> alunos = alunoService.listarPorCurso(id);

        Map<Long, List<Matricula>> matriculasPorAluno = new HashMap<>();
        for (Aluno aluno : alunos) {
            matriculasPorAluno.put(
                    aluno.getId(),
                    matriculaService.listarPorAluno(aluno.getId()));
        }

        model.addAttribute("curso", curso);
        model.addAttribute("alunos", alunos);
        model.addAttribute("matriculasPorAluno", matriculasPorAluno);
        model.addAttribute("disciplinas", disciplinaService.listarPorCurso(id));
        return "detalhe-curso";
    }

    // ==========================================
    // CADASTRAR
    // ==========================================
    @GetMapping("/novo")
    public String novo(Model model) {
        prepararFormulario(model, new Curso(), "Cadastrar Curso", "/cursos");
        return "formulario-curso";
    }

    @PostMapping
    public String cadastrar(
            @Valid @ModelAttribute("curso") Curso curso,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (cursoService.nomeEmUso(curso.getNome(), null)) {
            resultado.rejectValue("nome", "nome.duplicado",
                    "Já existe um curso com esse nome");
        }

        if (resultado.hasErrors()) {
            prepararFormulario(model, curso, "Cadastrar Curso", "/cursos");
            return "formulario-curso";
        }

        cursoService.cadastrar(curso);
        redirectAttributes.addFlashAttribute(
                "sucesso", "Curso cadastrado com sucesso.");
        return "redirect:/cursos";
    }

    // ==========================================
    // EDITAR
    // ==========================================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        if (curso == null) {
            return "redirect:/cursos";
        }
        prepararFormulario(model, curso, "Editar Curso",
                "/cursos/" + id + "/editar");
        return "formulario-curso";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("curso") Curso curso,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        curso.setId(id);

        if (cursoService.nomeEmUso(curso.getNome(), id)) {
            resultado.rejectValue("nome", "nome.duplicado",
                    "Já existe outro curso com esse nome");
        }

        if (resultado.hasErrors()) {
            prepararFormulario(model, curso, "Editar Curso",
                    "/cursos/" + id + "/editar");
            return "formulario-curso";
        }

        if (cursoService.atualizar(id, curso) == null) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Curso não encontrado.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Curso atualizado com sucesso.");
        }
        return "redirect:/cursos";
    }

    // ==========================================
    // EXCLUIR
    // ==========================================
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (cursoService.possuiVinculos(id)) {
            redirectAttributes.addFlashAttribute("erro",
                    "Não é possível excluir o curso, pois existem alunos "
                    + "ou disciplinas vinculados a ele.");
        } else if (cursoService.excluir(id)) {
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Curso excluído com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "erro", "Curso não encontrado.");
        }
        return "redirect:/cursos";
    }

    private void prepararFormulario(
            Model model, Curso curso, String titulo, String acao) {
        model.addAttribute("curso", curso);
        model.addAttribute("titulo", titulo);
        model.addAttribute("acao", acao);
    }
}
