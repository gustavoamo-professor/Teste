package com.example.jsonex.jsonexemplo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Tabela associativa entre Aluno e Disciplina (muitos-para-muitos).
 * A nota e a frequência pertencem à matrícula, não ao aluno
 * nem à disciplina.
 */
@Entity
@Table(name = "matriculas",
       uniqueConstraints = @UniqueConstraint(
               name = "uk_matricula_aluno_disciplina",
               columnNames = {"aluno_id", "disciplina_id"}))
public class Matricula {

    public static final double NOTA_MINIMA = 6.0;
    public static final int FREQUENCIA_MINIMA = 75;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitas matrículas podem pertencer ao mesmo aluno.
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Muitas matrículas podem estar relacionadas à mesma disciplina.
    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    // De 0 a 10 (null = ainda não lançada)
    @Column
    private Double nota;

    // Percentual de 0 a 100 (null = ainda não lançada)
    @Column
    private Integer frequencia;

    public Matricula() {
    }

    public Long getId() { return id; }
    public Aluno getAluno() { return aluno; }
    public Disciplina getDisciplina() { return disciplina; }
    public Double getNota() { return nota; }
    public Integer getFrequencia() { return frequencia; }

    public void setId(Long id) { this.id = id; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }
    public void setNota(Double nota) { this.nota = nota; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }

    /** Situação calculada (não é gravada no banco). */
    public String getSituacao() {
        if (nota == null || frequencia == null) {
            return "Cursando";
        }
        if (frequencia < FREQUENCIA_MINIMA) {
            return "Reprovado por falta";
        }
        return nota >= NOTA_MINIMA ? "Aprovado" : "Reprovado por nota";
    }

    /** Classe CSS da situação. */
    public String getSituacaoCss() {
        String s = getSituacao();
        if (s.equals("Aprovado")) {
            return "situacao-aprovado";
        }
        if (s.equals("Cursando")) {
            return "situacao-cursando";
        }
        return "situacao-reprovado";
    }
}
