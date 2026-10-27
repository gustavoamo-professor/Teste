package com.example.jsonex.jsonexemplo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CursoDTO {

    private Long id;

    @NotBlank(message = "O nome do curso não pode estar em branco")
    @Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres")
    private String nome;

    // Construtor padrão (obrigatório)
    public CursoDTO() {
    }

    // Construtor conveniente para conversões
    public CursoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
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
