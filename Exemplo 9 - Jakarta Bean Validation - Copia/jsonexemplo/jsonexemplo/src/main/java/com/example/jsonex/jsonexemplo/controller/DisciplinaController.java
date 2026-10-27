package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    public DisciplinaController(
            DisciplinaService disciplinaService,
            CursoService cursoService) {

        this.disciplinaService = disciplinaService;
        this.cursoService = cursoService;
    }


    // LISTAR
    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {

        model.addAttribute(
                "disciplinas",
                disciplinaService.listarTodos()
        );

        return "disciplinas";
    }


    // NOVA DISCIPLINA
    @GetMapping("/disciplinas/novo")
    public String novaDisciplina(Model model) {

        model.addAttribute(
                "disciplina",
                new Disciplina()
        );

        model.addAttribute(
                "cursos",
                cursoService.listarTodos()
        );

        model.addAttribute(
                "titulo",
                "Cadastrar Disciplina"
        );

        return "formulario-disciplina";
    }


    // CADASTRAR
    @PostMapping("/disciplinas")
    public String cadastrarDisciplina(

            @Valid
            @ModelAttribute("disciplina")
            Disciplina disciplina,

            BindingResult resultado,

            @RequestParam(
                name = "cursoId",
                required = false
            )
            Long cursoId,

            Model model,

            RedirectAttributes redirectAttributes) {


        // Verifica curso
        Curso curso = null;

        if (cursoId != null) {
            curso = cursoService.buscarPorId(cursoId);
        }


        if (curso == null) {

            resultado.reject(
                    "curso.obrigatorio",
                    "Selecione um curso válido"
            );
        }


        // Nome duplicado
        if (disciplinaService
                .nomeJaCadastrado(
                        disciplina.getNome())) {

            resultado.rejectValue(
                    "nome",
                    "nome.duplicado",
                    "Já existe uma disciplina com esse nome"
            );
        }


        if (resultado.hasErrors()) {

            model.addAttribute(
                    "cursos",
                    cursoService.listarTodos()
            );

            model.addAttribute(
                    "titulo",
                    "Cadastrar Disciplina"
            );

            return "formulario-disciplina";
        }


        // ASSOCIA O CURSO À DISCIPLINA
        disciplina.setCurso(curso);


        disciplinaService.cadastrar(
                disciplina
        );


        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Disciplina cadastrada com sucesso."
        );


        return "redirect:/disciplinas";
    }


    // ABRIR EDIÇÃO
    @GetMapping("/disciplinas/{id}/editar")
    public String editarDisciplina(
            @PathVariable Long id,
            Model model) {

        Disciplina disciplina =
                disciplinaService.buscarPorId(id);


        if (disciplina == null) {
            return "redirect:/disciplinas";
        }


        model.addAttribute(
                "disciplina",
                disciplina
        );

        model.addAttribute(
                "cursos",
                cursoService.listarTodos()
        );

        model.addAttribute(
                "titulo",
                "Editar Disciplina"
        );


        return "formulario-disciplina";
    }


    // ATUALIZAR
    @PostMapping("/disciplinas/{id}/editar")
    public String atualizarDisciplina(

            @PathVariable Long id,

            @Valid
            @ModelAttribute("disciplina")
            Disciplina disciplina,

            BindingResult resultado,

            @RequestParam(
                name = "cursoId",
                required = false
            )
            Long cursoId,

            Model model,

            RedirectAttributes redirectAttributes) {


        disciplina.setId(id);


        Curso curso = null;

        if (cursoId != null) {
            curso = cursoService.buscarPorId(cursoId);
        }


        if (curso == null) {

            resultado.reject(
                    "curso.obrigatorio",
                    "Selecione um curso válido"
            );
        }


        if (disciplinaService
                .nomeJaCadastradoPorOutra(
                        disciplina.getNome(),
                        id)) {

            resultado.rejectValue(
                    "nome",
                    "nome.duplicado",
                    "Já existe outra disciplina com esse nome"
            );
        }


        if (resultado.hasErrors()) {

            model.addAttribute(
                    "cursos",
                    cursoService.listarTodos()
            );

            model.addAttribute(
                    "titulo",
                    "Editar Disciplina"
            );

            return "formulario-disciplina";
        }


        // ASSOCIA O CURSO
        disciplina.setCurso(curso);


        disciplinaService.atualizar(
                id,
                disciplina
        );


        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Disciplina atualizada com sucesso."
        );


        return "redirect:/disciplinas";
    }


    // EXCLUIR
    @PostMapping("/disciplinas/{id}/excluir")
    public String excluirDisciplina(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {


        if (disciplinaService.possuiMatriculas(id)) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não é possível excluir uma disciplina que possui matrículas."
            );

            return "redirect:/disciplinas";
        }


        disciplinaService.excluir(id);


        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Disciplina excluída com sucesso."
        );


        return "redirect:/disciplinas";
    }
}

