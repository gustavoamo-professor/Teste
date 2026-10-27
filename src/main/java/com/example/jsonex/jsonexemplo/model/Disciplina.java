package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve possuir entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "A carga horária é obrigatória")
    @Min(value = 1, message = "A carga horária deve ser maior que zero")
    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    public Disciplina() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}