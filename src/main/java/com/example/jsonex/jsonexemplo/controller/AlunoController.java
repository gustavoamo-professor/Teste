package com.example.jsonex.jsonexemplo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.service.AlunoService;

@Controller
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // ==========================================
    // LISTAR / PESQUISAR
    // ==========================================

    @GetMapping("/alunos")
    public String listarAlunos(
            @RequestParam(
                name = "nome",
                required = false
            )
            String nome,
            Model model) {

        var alunos =
            alunoService.pesquisarPorNome(nome);

        model.addAttribute(
            "alunos",
            alunos
        );

        model.addAttribute(
            "nomePesquisa",
            nome
        );

        boolean realizouPesquisa =
            nome != null && !nome.isBlank();

        model.addAttribute(
            "semResultados",
            realizouPesquisa && alunos.isEmpty()
        );

        return "alunos";
    }

    // ==========================================
    // NOVO
    // ==========================================

    @GetMapping("/alunos/novo")
    public String novoAluno(Model model) {

        model.addAttribute(
            "aluno",
            new Aluno()
        );

        model.addAttribute(
            "titulo",
            "Cadastrar Aluno"
        );

        return "formulario-aluno";
    }

    // ==========================================
    // CADASTRAR COM VALIDAÇÃO
    // ==========================================

    @PostMapping("/alunos")
    public String cadastrarAluno(

            @Valid
            @ModelAttribute("aluno")
            Aluno aluno,

            BindingResult resultado,

            Model model) {

        // Se houver erros de validação,
        // não salva no banco.
        if (resultado.hasErrors()) {

            model.addAttribute(
                "titulo",
                "Cadastrar Aluno"
            );

            return "formulario-aluno";
        }

        alunoService.cadastrar(aluno);

        return "redirect:/alunos";
    }

    // ==========================================
    // ABRIR EDIÇÃO
    // ==========================================

    @GetMapping("/alunos/{id}/editar")
    public String editarAluno(
            @PathVariable Long id,
            Model model) {

        Aluno aluno =
            alunoService.buscarPorId(id);

        if (aluno == null) {
            return "redirect:/alunos";
        }

        model.addAttribute(
            "aluno",
            aluno
        );

        model.addAttribute(
            "titulo",
            "Editar Aluno"
        );

        return "formulario-aluno";
    }

    // ==========================================
    // ATUALIZAR COM VALIDAÇÃO
    // ==========================================

    @PostMapping("/alunos/{id}/editar")
    public String atualizarAluno(

            @PathVariable Long id,

            @Valid
            @ModelAttribute("aluno")
            Aluno aluno,

            BindingResult resultado,

            Model model) {

        // Mantém o ID no objeto caso
        // precisemos retornar ao formulário.
        aluno.setId(id);

        if (resultado.hasErrors()) {

            model.addAttribute(
                "titulo",
                "Editar Aluno"
            );

            return "formulario-aluno";
        }

        alunoService.atualizar(
            id,
            aluno
        );

        return "redirect:/alunos";
    }

    // ==========================================
    // EXCLUIR
    // ==========================================

    @PostMapping("/alunos/{id}/excluir")
    public String excluirAluno(
            @PathVariable Long id) {

        alunoService.excluir(id);

        return "redirect:/alunos";
    }
}