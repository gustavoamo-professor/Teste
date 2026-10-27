package com.example.jsonex.jsonexemplo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CursoDTO {

    private Long id;

    @NotBlank(message = "O nome do curso é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do curso deve possuir entre 2 e 100 caracteres")
    private String nome;

    public CursoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
