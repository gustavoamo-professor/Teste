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

import com.example.jsonex.jsonexemplo.dto.CursoDTO;
import com.example.jsonex.jsonexemplo.service.CursoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listarCursos(
            @RequestParam(name = "nome", required = false) String nome,
            Model model) {

        var cursos = cursoService.pesquisarPorNome(nome);

        model.addAttribute("cursos", cursos);
        model.addAttribute("nomePesquisa", nome);

        boolean realizouPesquisa = nome != null && !nome.isBlank();

        model.addAttribute("semResultados", realizouPesquisa && cursos.isEmpty());

        return "cursos";
    }

    @GetMapping("/novo")
    public String novoCurso(Model model) {
        model.addAttribute("curso", new CursoDTO());
        model.addAttribute("titulo", "Cadastrar Curso");
        return "formulario-curso";
    }

    @PostMapping
    public String cadastrarCurso(
            @Valid @ModelAttribute("curso") CursoDTO cursoDTO,
            BindingResult resultado,
            Model model) {

        if (cursoService.nomeJaCadastrado(cursoDTO.getNome())) {
            resultado.rejectValue("nome", "nome.duplicado", "Este curso já está cadastrado");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Cadastrar Curso");
            return "formulario-curso";
        }

        cursoService.cadastrar(cursoDTO);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    public String editarCurso(@PathVariable Long id, Model model) {
        CursoDTO cursoDTO = cursoService.buscarDTOPorId(id);

        if (cursoDTO == null) {
            return "redirect:/cursos";
        }

        model.addAttribute("curso", cursoDTO);
        model.addAttribute("titulo", "Editar Curso");
        return "formulario-curso";
    }

    @PostMapping("/{id}/editar")
    public String atualizarCurso(
            @PathVariable Long id,
            @Valid @ModelAttribute("curso") CursoDTO cursoDTO,
            BindingResult resultado,
            Model model) {

        cursoDTO.setId(id);

        if (cursoService.nomeJaCadastradoPorOutro(cursoDTO.getNome(), id)) {
            resultado.rejectValue("nome", "nome.duplicado", "Este curso já pertence a outro registro");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Editar Curso");
            return "formulario-curso";
        }

        cursoService.atualizar(id, cursoDTO);
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/excluir")
    public String excluirCurso(@PathVariable Long id) {
        cursoService.excluir(id);
        return "redirect:/cursos";
    }
}
