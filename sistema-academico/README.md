# Sistema de Gestão Acadêmica (Spring Boot + Thymeleaf)

Implementação do material **MuitosPraMuitos.docx**: relação muitos-para-muitos
entre **Aluno** e **Disciplina** através da entidade **Matrícula**
(a nota e a frequência pertencem à matrícula).

```
Curso
 ├── Alunos
 │     └── Matrículas
 │            └── Disciplinas
 └── Disciplinas
```

## Como rodar

Requisitos: **Java 17+** e **Maven** (ou abra a pasta no IntelliJ/Eclipse/VS Code).

```bash
mvn spring-boot:run
```

Abra http://localhost:8080 — já vem com dados de exemplo (curso, alunos,
disciplinas e matrículas; o Gustavo está em Java com nota 8.5 e 92% de frequência).

- Banco: **H2 em arquivo** (`./data/academico`), console em `/h2-console`
  (JDBC URL `jdbc:h2:file:./data/academico`, usuário `sa`, senha vazia).
- Para usar MySQL, veja os comentários em `application.properties`.
- Para zerar o banco, apague a pasta `data/`.

## Telas

| Rota | O que faz |
|---|---|
| `/alunos` | CRUD de alunos + pesquisa por nome |
| `/cursos` | CRUD de cursos; `/cursos/{id}` mostra a árvore Curso → Alunos → Matrículas |
| `/disciplinas` | CRUD de disciplinas (cada uma pertence a um curso) |
| `/matriculas` | CRUD de matrículas; `/matriculas?alunoId=1` filtra por aluno |

## Regras de negócio

- Aluno só se matricula em disciplinas **do próprio curso**.
- Não existe matrícula duplicada (validado no serviço **e** por constraint única no banco).
- Não é possível excluir: curso com alunos/disciplinas, aluno com matrículas,
  disciplina com matrículas.
- Não é possível trocar o curso de aluno/disciplina que já tenha matrículas.
- Nome de disciplina é único **dentro do curso**; e-mail e RA de aluno são únicos.
- Situação da matrícula (calculada): *Cursando* (sem nota/frequência), *Aprovado*
  (nota ≥ 6 e frequência ≥ 75%), *Reprovado por nota* ou *Reprovado por falta*.

## Erros encontrados no material original (e como foram corrigidos)

1. **`││` no lugar de `||`** em `MatriculaService` e `DisciplinaService`
   (caractere de desenho copiado do Word) → não compilava.
2. **`Matriculas.html` aparecia duas vezes** (um trecho solto e a página completa);
   mantida só a página completa, agora com o CRUD.
3. **`matricular()` devolvia `null` em silêncio** (duplicada, aluno ou disciplina
   inexistente) e o controller ignorava o retorno → agora lança
   `RegraNegocioException` e a tela mostra o motivo.
4. **Filtro `?alunoId=` quebrado**: `alunos.html` linkava para
   `/matriculas?alunoId=...`, mas o controller ignorava o parâmetro.
5. **Links para `/cursos` sem Curso**: o menu apontava para uma tela que não existia.
6. **`Aluno.matricula` confundia com a entidade `Matricula`** → campo renomeado
   para `ra` (registro acadêmico).
7. **`Matricula` sem nota/frequência/situação**, que o próprio diagrama do material
   coloca na matrícula.
8. **Sem unicidade no banco** para (aluno, disciplina) → `@UniqueConstraint`.
9. **`DisciplinaService.excluir` devolvia `false` para dois motivos diferentes**
   (não existe / tem matrículas); o controller precisava checar antes. Agora cada
   caso tem sua mensagem.
10. **Formulário de disciplina duplicado** (um para cadastro, outro para edição)
    → um formulário só, com a URL vinda do controller.
11. **Nome de disciplina único globalmente** → agora único por curso.
12. **Cabeçalho/menu copiados em todas as páginas** → `fragments.html`.
13. Arquivos que o material não trazia mas usa (`Aluno`, `Curso`, `AlunoService`,
    `AlunoRepository`, `AlunoController`, `pom.xml`, `application.properties`)
    foram criados.
