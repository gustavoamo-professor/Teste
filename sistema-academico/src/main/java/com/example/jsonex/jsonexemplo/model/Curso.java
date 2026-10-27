package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do curso é obrigatório")
    @Size(min = 3, max = 100,
          message = "O nome deve possuir entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    public Curso() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
}
