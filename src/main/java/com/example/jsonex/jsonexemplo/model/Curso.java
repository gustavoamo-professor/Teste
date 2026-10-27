package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do curso é obrigatório")
    private String nome;

    @OneToMany(mappedBy = "curso")
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}