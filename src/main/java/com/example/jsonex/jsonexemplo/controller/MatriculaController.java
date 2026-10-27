package com.example.jsonex.jsonexemplo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public MatriculaController(MatriculaService matriculaService, AlunoService alunoService, DisciplinaService disciplinaService) {
        this.matriculaService = matriculaService;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
    }
    @GetMapping("/matriculas")
    public String listar(Model model) {
        model.addAttribute("matriculas", matriculaService.listarTodos());
        return "matriculas";
    }
    @GetMapping("/matriculas/novo")
    public String novaMatricula(Model model) {
        model.addAttribute("matricula", new MatriculaDTO());
        carregarDadosFormulario(model);
        model.addAttribute("titulo", "Nova Matrícula");
        return "formulario-matricula";
    }
    @PostMapping("/matriculas")
    public String cadastrar(@Valid @ModelAttribute("matricula") MatriculaDTO matriculaDTO, BindingResult resultado,
            Model model, RedirectAttributes redirectAttributes) {
        if (matriculaService.matriculaJaExiste(matriculaDTO.getAlunoId(), matriculaDTO.getDisciplinaId()))
            resultado.reject("matricula.duplicada", "O aluno já está matriculado nesta disciplina.");
        if (matriculaDTO.getAlunoId() != null && matriculaDTO.getDisciplinaId() != null &&
                !matriculaService.alunoEDisciplinaDoMesmoCurso(matriculaDTO.getAlunoId(), matriculaDTO.getDisciplinaId()))
            resultado.reject("curso.invalido", "O aluno só pode ser matriculado em disciplinas do seu curso.");
        if (resultado.hasErrors()) {
            carregarDadosFormulario(model);
            model.addAttribute("titulo", "Nova Matrícula");
            return "formulario-matricula";
        }
        matriculaService.cadastrar(matriculaDTO);
        redirectAttributes.addFlashAttribute("sucesso", "Matrícula realizada com sucesso.");
        return "redirect:/matriculas";
    }
    @PostMapping("/matriculas/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (matriculaService.excluir(id)) redirectAttributes.addFlashAttribute("sucesso", "Matrícula excluída com sucesso.");
        else redirectAttributes.addFlashAttribute("erro", "Matrícula não encontrada.");
        return "redirect:/matriculas";
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("disciplinas", disciplinaService.listarTodos());
    }
}
