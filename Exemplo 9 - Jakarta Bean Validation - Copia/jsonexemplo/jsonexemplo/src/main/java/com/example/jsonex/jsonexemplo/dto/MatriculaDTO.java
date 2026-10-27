package com.example.jsonex.jsonexemplo.dto;

import jakarta.validation.constraints.NotNull;

public class MatriculaDTO {

    private Long id;

    @NotNull(message = "Selecione um aluno")
    private Long alunoId;

    @NotNull(message = "Selecione uma disciplina")
    private Long disciplinaId;

    public MatriculaDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
}
