package com.example.jsonex.jsonexemplo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Dados do formulário de matrícula (cadastro e edição). */
public class MatriculaForm {

    private Long id;

    @NotNull(message = "Selecione o aluno")
    private Long alunoId;

    @NotNull(message = "Selecione a disciplina")
    private Long disciplinaId;

    @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0")
    @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10")
    private Double nota;

    @Min(value = 0, message = "A frequência deve ser no mínimo 0")
    @Max(value = 100, message = "A frequência deve ser no máximo 100")
    private Integer frequencia;

    public MatriculaForm() {
    }

    public Long getId() { return id; }
    public Long getAlunoId() { return alunoId; }
    public Long getDisciplinaId() { return disciplinaId; }
    public Double getNota() { return nota; }
    public Integer getFrequencia() { return frequencia; }

    public void setId(Long id) { this.id = id; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }
    public void setDisciplinaId(Long disciplinaId) { this.disciplinaId = disciplinaId; }
    public void setNota(Double nota) { this.nota = nota; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }
}
