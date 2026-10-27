package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 1, message = "A idade deve ser maior que zero")
    private Integer idade;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    private String matricula;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "aluno")
    private List<Matricula> matriculas = new ArrayList<>();

    public Aluno() {
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

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public String getCursoNome() {
        return curso != null ? curso.getNome() : "Sem curso";
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
}