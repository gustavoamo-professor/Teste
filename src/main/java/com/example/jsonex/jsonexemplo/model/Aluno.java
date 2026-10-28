package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(
        min = 3,
        max = 100,
        message = "O nome deve possuir entre 3 e 100 caracteres"
    )
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 14, message = "A idade mínima é 14 anos")
    @Max(value = 100, message = "A idade máxima é 100 anos")
    private Integer idade;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(
        min = 4,
        max = 20,
        message = "A matrícula deve possuir entre 4 e 20 caracteres"
    )
    @Column(unique = true, nullable = false)
    private String matricula;

    public Aluno() {
    }

    public Aluno(
            String nome,
            Integer idade,
            String curso,
            String email,
            String matricula) {

        this.nome = nome;
        this.idade = idade;
        this.curso = curso;
        this.email = email;
        this.matricula = matricula;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getCurso() {
        return curso;
    }

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}