package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jsonex.jsonexemplo.dto.MatriculaDTO;
import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.DisciplinaService;
import com.example.jsonex.jsonexemplo.service.MatriculaService;

import jakarta.validation.Valid;

@Controller
public class MatriculaController {

    private final MatriculaService matriculaService;

    private final AlunoService alunoService;

    private final DisciplinaService disciplinaService;

    public MatriculaController(
            MatriculaService matriculaService,
            AlunoService alunoService,
            DisciplinaService disciplinaService) {

        this.matriculaService =
                matriculaService;

        this.alunoService =
                alunoService;

        this.disciplinaService =
                disciplinaService;
    }

    // ==========================================
    // LISTAR
    // ==========================================

    @GetMapping("/matriculas")
    public String listar(
            Model model) {

        model.addAttribute(
                "matriculas",
                matriculaService.listarTodos()
        );

        return "matriculas";
    }

    // ==========================================
    // NOVA MATRÍCULA
    // ==========================================

    @GetMapping("/matriculas/novo")
    public String novaMatricula(
            Model model) {

        model.addAttribute(
                "matricula",
                new MatriculaDTO()
        );

        carregarDadosFormulario(
                model
        );

        model.addAttribute(
                "titulo",
                "Nova Matrícula"
        );

        return "formulario-matricula";
    }

    // ==========================================
    // CADASTRAR
    // ==========================================

    @PostMapping("/matriculas")
    public String cadastrar(

            @Valid
            @ModelAttribute("matricula")
            MatriculaDTO matriculaDTO,

            BindingResult resultado,

            Model model,

            RedirectAttributes redirectAttributes) {

        // ======================================
        // MATRÍCULA DUPLICADA
        // ======================================

        if (matriculaService
                .matriculaJaExiste(
                        matriculaDTO.getAlunoId(),
                        matriculaDTO.getDisciplinaId())) {

            resultado.reject(
                    "matricula.duplicada",
                    "O aluno já está matriculado nesta disciplina."
            );
        }

        // ======================================
        // CURSO DIFERENTE
        // ======================================

        if (matriculaDTO.getAlunoId() != null &&
                matriculaDTO.getDisciplinaId() != null &&
                !matriculaService
                    .alunoEDisciplinaDoMesmoCurso(
                        matriculaDTO.getAlunoId(),
                        matriculaDTO.getDisciplinaId())) {

            resultado.reject(
                    "curso.invalido",
                    "O aluno só pode ser matriculado em disciplinas do seu curso."
            );
        }

        // ======================================
        // ERROS
        // ======================================

        if (resultado.hasErrors()) {

            carregarDadosFormulario(
                    model
            );

            model.addAttribute(
                    "titulo",
                    "Nova Matrícula"
            );

            return "formulario-matricula";
        }

        matriculaService.cadastrar(
                matriculaDTO
        );

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Matrícula realizada com sucesso."
        );

        return "redirect:/matriculas";
    }

    // ==========================================
    // ABRIR EDIÇÃO
    // ==========================================

    @GetMapping("/matriculas/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model) {

        MatriculaDTO matricula =
                matriculaService
                    .buscarDTOporId(id);

        if (matricula == null) {

            return "redirect:/matriculas";
        }

        model.addAttribute(
                "matricula",
                matricula
        );

        carregarDadosFormulario(
                model
        );

        model.addAttribute(
                "titulo",
                "Editar Matrícula"
        );

        return "formulario-matricula";
    }

    // ==========================================
    // ATUALIZAR
    // ==========================================

    @PostMapping("/matriculas/{id}/editar")
    public String atualizar(

            @PathVariable Long id,

            @Valid
            @ModelAttribute("matricula")
            MatriculaDTO matriculaDTO,

            BindingResult resultado,

            Model model,

            RedirectAttributes redirectAttributes) {

        matriculaDTO.setId(id);

        // Verifica duplicação
        if (matriculaService
                .matriculaJaExisteEmOutra(
                        matriculaDTO.getAlunoId(),
                        matriculaDTO.getDisciplinaId(),
                        id)) {

            resultado.reject(
                    "matricula.duplicada",
                    "O aluno já está matriculado nesta disciplina."
            );
        }

        // Verifica curso
        if (matriculaDTO.getAlunoId() != null &&
                matriculaDTO.getDisciplinaId() != null &&
                !matriculaService
                    .alunoEDisciplinaDoMesmoCurso(
                        matriculaDTO.getAlunoId(),
                        matriculaDTO.getDisciplinaId())) {

            resultado.reject(
                    "curso.invalido",
                    "O aluno só pode ser matriculado em disciplinas do seu curso."
            );
        }

        if (resultado.hasErrors()) {

            carregarDadosFormulario(
                    model
            );

            model.addAttribute(
                    "titulo",
                    "Editar Matrícula"
            );

            return "formulario-matricula";
        }

        matriculaService.atualizar(
                id,
                matriculaDTO
        );

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Matrícula atualizada com sucesso."
        );

        return "redirect:/matriculas";
    }

    // ==========================================
    // EXCLUIR
    // ==========================================

    @PostMapping("/matriculas/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        boolean excluiu =
                matriculaService.excluir(id);

        if (excluiu) {

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Matrícula excluída com sucesso."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Matrícula não encontrada."
            );
        }

        return "redirect:/matriculas";
    }

    // ==========================================
    // CARREGAR ALUNOS E DISCIPLINAS
    // ==========================================

    private void carregarDadosFormulario(
            Model model) {

        model.addAttribute(
                "alunos",
                alunoService.listarTodos()
        );

        model.addAttribute(
                "disciplinas",
                disciplinaService.listarTodos()
        );
    }
}
