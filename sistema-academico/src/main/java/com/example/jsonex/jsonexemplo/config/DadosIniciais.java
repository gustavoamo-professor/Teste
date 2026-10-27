package com.example.jsonex.jsonexemplo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.jsonex.jsonexemplo.model.Aluno;
import com.example.jsonex.jsonexemplo.model.Curso;
import com.example.jsonex.jsonexemplo.model.Disciplina;
import com.example.jsonex.jsonexemplo.repository.AlunoRepository;
import com.example.jsonex.jsonexemplo.repository.CursoRepository;
import com.example.jsonex.jsonexemplo.repository.DisciplinaRepository;
import com.example.jsonex.jsonexemplo.service.MatriculaService;

/** Carrega dados de exemplo só se o banco estiver vazio. */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final CursoRepository cursoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final MatriculaService matriculaService;

    public DadosIniciais(
            CursoRepository cursoRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository,
            MatriculaService matriculaService) {
        this.cursoRepository = cursoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.matriculaService = matriculaService;
    }

    @Override
    public void run(String... args) {
        if (cursoRepository.count() > 0) {
            return;
        }

        Curso si = criarCurso("Sistemas de Informação");
        Curso adm = criarCurso("Administração");

        Disciplina java = criarDisciplina("Java", 80, si);
        Disciplina bd = criarDisciplina("Banco de Dados", 60, si);
        criarDisciplina("Contabilidade", 60, adm);

        Aluno gustavo = criarAluno(
                "Gustavo", 20, "gustavo@exemplo.com", "RA0001", si);
        Aluno maria = criarAluno(
                "Maria Souza", 22, "maria@exemplo.com", "RA0002", si);
        criarAluno("Carlos Lima", 25, "carlos@exemplo.com", "RA0003", adm);

        // Exemplo do material: Gustavo, Java, nota 8.5, frequência 92%
        matriculaService.matricular(
                gustavo.getId(), java.getId(), 8.5, 92);
        matriculaService.matricular(
                gustavo.getId(), bd.getId(), null, null);
        matriculaService.matricular(
                maria.getId(), java.getId(), 5.0, 80);
    }

    private Curso criarCurso(String nome) {
        Curso c = new Curso();
        c.setNome(nome);
        return cursoRepository.save(c);
    }

    private Disciplina criarDisciplina(String nome, int carga, Curso curso) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCargaHoraria(carga);
        d.setCurso(curso);
        return disciplinaRepository.save(d);
    }

    private Aluno criarAluno(
            String nome, int idade, String email, String ra, Curso curso) {
        Aluno a = new Aluno();
        a.setNome(nome);
        a.setIdade(idade);
        a.setEmail(email);
        a.setRa(ra);
        a.setCurso(curso);
        return alunoRepository.save(a);
    }
}
