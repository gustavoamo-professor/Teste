package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @NotBlank(message = "O nome do aluno é obrigatório")
    @Size(min = 3, max = 100,
          message = "O nome deve possuir entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 1, message = "A idade deve ser maior que zero")
    @Max(value = 120, message = "A idade deve ser no máximo 120")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Column(nullable = false, length = 150)
    private String email;

    // Registro acadêmico (RA) do aluno.
    // Obs.: no material original este campo se chamava "matricula",
    // o que se confundia com a entidade Matricula (aluno x disciplina).
    @NotBlank(message = "O RA é obrigatório")
    @Size(max = 30, message = "O RA deve ter no máximo 30 caracteres")
    @Column(nullable = false, length = 30)
    private String ra;

    // Vários alunos pertencem ao mesmo curso.
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    public Aluno() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getIdade() { return idade; }
    public String getEmail() { return email; }
    public String getRa() { return ra; }
    public Curso getCurso() { return curso; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public void setEmail(String email) { this.email = email; }
    public void setRa(String ra) { this.ra = ra; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public String getCursoNome() {
        return curso != null ? curso.getNome() : "";
    }
}
