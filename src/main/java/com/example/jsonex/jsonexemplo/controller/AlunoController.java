package com.example.jsonex.jsonexemplo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jsonex.jsonexemplo.dto.AlunoDTO;
import com.example.jsonex.jsonexemplo.service.AlunoService;
import com.example.jsonex.jsonexemplo.service.CursoService;

import jakarta.validation.Valid;

@Controller
public class AlunoController {

    private final AlunoService alunoService;
    private final CursoService cursoService; // Adicionado CursoService

    // Injeção de dependência atualizada
    public AlunoController(AlunoService alunoService, CursoService cursoService) {
        this.alunoService = alunoService;
        this.cursoService = cursoService;
    }

    // ==================================================
    // LISTAR / PESQUISAR
    // ==================================================

    @GetMapping("/alunos")
    public String listarAlunos(
            @RequestParam(name = "nome", required = false) String nome,
            Model model) {

        var alunos = alunoService.pesquisarPorNome(nome);

        model.addAttribute("alunos", alunos);
        model.addAttribute("nomePesquisa", nome);

        boolean realizouPesquisa =
                nome != null && !nome.isBlank();

        model.addAttribute(
                "semResultados",
                realizouPesquisa && alunos.isEmpty()
        );

        return "alunos";
    }

    // ==================================================
    // ABRIR FORMULÁRIO DE CADASTRO
    // ==================================================

    @GetMapping("/alunos/novo")
    public String novoAluno(Model model) {

        model.addAttribute(
                "aluno",
                new AlunoDTO()
        );

        // Disponibilizar os cursos no formulário
        model.addAttribute(
                "cursos",
                cursoService.listarTodos()
        );

        model.addAttribute(
                "titulo",
                "Cadastrar Aluno"
        );

        return "formulario-aluno";
    }

    // ==================================================
    // CADASTRAR
    // ==================================================

    @PostMapping("/alunos")
    public String cadastrarAluno(
            @Valid
            @ModelAttribute("aluno")
            AlunoDTO alunoDTO,
            BindingResult resultado,
            Model model) {

        // ==============================================
        // REGRA DE NEGÓCIO: EMAIL ÚNICO
        // ==============================================
        if (alunoService.emailJaCadastrado(alunoDTO.getEmail())) {
            resultado.rejectValue(
                    "email",
                    "email.duplicado",
                    "Este e-mail já está cadastrado"
            );
        }

        // ==============================================
        // REGRA DE NEGÓCIO: MATRÍCULA ÚNICA
        // ==============================================
        if (alunoService.matriculaJaCadastrada(alunoDTO.getMatricula())) {
            resultado.rejectValue(
                    "matricula",
                    "matricula.duplicada",
                    "Esta matrícula já está cadastrada"
            );
        }

        // ==============================================
        // VERIFICA ERROS
        // ==============================================
        if (resultado.hasErrors()) {
            model.addAttribute(
                    "titulo",
                    "Cadastrar Aluno"
            );
            // Reenvia a lista de cursos em caso de erro
            model.addAttribute(
                    "cursos",
                    cursoService.listarTodos()
            );

            return "formulario-aluno";
        }

        // Service recebe o DTO, transforma em Aluno e salva no banco.
        alunoService.cadastrar(alunoDTO);

        return "redirect:/alunos";
    }

    // ==================================================
    // ABRIR FORMULÁRIO DE EDIÇÃO
    // ==================================================

    @GetMapping("/alunos/{id}/editar")
    public String editarAluno(
            @PathVariable Long id,
            Model model) {

        AlunoDTO alunoDTO = alunoService.buscarDTOporId(id);

        if (alunoDTO == null) {
            return "redirect:/alunos";
        }

        model.addAttribute(
                "aluno",
                alunoDTO
        );

        // Disponibilizar os cursos no formulário
        model.addAttribute(
                "cursos",
                cursoService.listarTodos()
        );

        model.addAttribute(
                "titulo",
                "Editar Aluno"
        );

        return "formulario-aluno";
    }

    // ==================================================
    // SALVAR ALTERAÇÃO
    // ==================================================

    @PostMapping("/alunos/{id}/editar")
    public String atualizarAluno(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("aluno")
            AlunoDTO alunoDTO,
            BindingResult resultado,
            Model model) {
        
        // Garante que o DTO mantenha o ID do registro em edição.
        alunoDTO.setId(id);

        // ==============================================
        // EMAIL DE OUTRO ALUNO?
        // ==============================================
        if (alunoService.emailJaCadastradoPorOutro(alunoDTO.getEmail(), id)) {
            resultado.rejectValue(
                    "email",
                    "email.duplicado",
                    "Este e-mail já pertence a outro aluno"
            );
        }

        // ==============================================
        // MATRÍCULA DE OUTRO ALUNO?
        // ==============================================
        if (alunoService.matriculaJaCadastradaPorOutro(alunoDTO.getMatricula(), id)) {
            resultado.rejectValue(
                    "matricula",
                    "matricula.duplicada",
                    "Esta matrícula já pertence a outro aluno"
            );
        }

        // ==============================================
        // VERIFICA ERROS
        // ==============================================
        if (resultado.hasErrors()) {
            model.addAttribute(
                    "titulo",
                    "Editar Aluno"
            );
            // Reenvia a lista de cursos em caso de erro
            model.addAttribute(
                    "cursos",
                    cursoService.listarTodos()
            );

            return "formulario-aluno";
        }

        // Service recebe DTO e atualiza a entidade correspondente.
        alunoService.atualizar(id, alunoDTO);

        return "redirect:/alunos";
    }

    // ==================================================
    // EXCLUIR
    // ==================================================
    @PostMapping("/alunos/{id}/excluir")
    public String excluirAluno(@PathVariable Long id) {
        alunoService.excluir(id);
        return "redirect:/alunos";
    }
}