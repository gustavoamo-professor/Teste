package com.example.jsonex.jsonexemplo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AlunoDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve possuir entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 14, message = "A idade mínima é 14 anos")
    @Max(value = 100, message = "A idade máxima é 100 anos")
    private Integer idade;

    @NotBlank(message = "O curso é obrigatório")
    @Size(min = 2, max = 100, message = "O curso deve possuir entre 2 e 100 caracteres")
    private String curso;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(min = 4, max = 20, message = "A matrícula deve possuir entre 4 e 20 caracteres")
    private String matricula;

    public AlunoDTO() {
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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    public String getCursoNome() {
        return curso;
    }

    public void setCursoNome(String cursoNome) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
