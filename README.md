# API de Cursos e Matrículas

**Alunos:** Emanuel Hennig e Guilherme Lago 

API REST do Trabalho Prático 2 para cadastrar cursos, alunos e matrículas. Desenvolvida com Java 21, Spring Boot, Spring Data JPA, Bean Validation, Lombok e banco H2.

## Como executar

É necessário ter um JDK 21 instalado e a variável `JAVA_HOME` apontando para ele. O Maven Wrapper está incluído; o primeiro uso precisa de internet para baixar o Maven e as dependências.

Abra um terminal na pasta `api-cursos` e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
sh mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

Se o JDK no Windows apresentar `Unable to establish loopback connection`, execute no terminal antes de iniciar:

```powershell
$env:JAVA_TOOL_OPTIONS = "-Djdk.net.unixdomain.tmpdir=./.java-sockets"
```

Esse ajuste contorna um problema de sockets do JDK observado no ambiente de teste. Não crie a pasta `.java-sockets`.

Para gerar o executável:

```powershell
.\mvnw.cmd clean package
java -jar target/api-cursos-0.0.1-SNAPSHOT.jar
```

O H2 salva os dados no arquivo `cursos_db.mv.db`, na pasta de execução. Os dados permanecem após reiniciar a aplicação. O banco é criado automaticamente.

Console do H2: `http://localhost:8080/h2-console`.

- JDBC URL: `jdbc:h2:file:./cursos_db`
- Usuário: `sa`
- Senha: deixe em branco

## Organização

- `controllers`: rotas REST e regras de negócio.
- `repositories`: persistência com `JpaRepository`.
- `models`: entidades e validações.
- `dtos`: dados de entrada para criar matrícula e atualizar seu status.
- `exceptions`: tratamento de erros.

Curso e Aluno possuem `@OneToMany` para matrículas. Matrícula possui `@ManyToOne` para Aluno e Curso. As listas inversas são ignoradas no JSON para evitar referências circulares.

## Endpoints

| Método | Rota | Operação |
| --- | --- | --- |
| POST | `/cursos` | Cadastrar curso |
| GET | `/cursos` | Listar cursos com paginação |
| GET | `/cursos/{id}` | Buscar curso |
| PUT | `/cursos/{id}` | Atualizar curso |
| DELETE | `/cursos/{id}` | Remover curso sem matrículas |
| POST | `/alunos` | Cadastrar aluno |
| GET | `/alunos` | Listar alunos com paginação |
| GET | `/alunos/{id}` | Buscar aluno |
| PUT | `/alunos/{id}` | Atualizar aluno |
| DELETE | `/alunos/{id}` | Remover aluno sem matrículas |
| POST | `/matriculas` | Realizar matrícula |
| GET | `/matriculas` | Listar matrículas com paginação |
| GET | `/matriculas/{id}` | Buscar matrícula |
| PUT | `/matriculas/{id}` | Atualizar somente o status |
| DELETE | `/matriculas/{id}` | Cancelar, removendo a matrícula |

Todas as listagens recebem `page`, `size` e `sort`, usando `Pageable`. A primeira página é a de número zero. Os registros ficam no campo `content` da resposta.

```text
GET /cursos?page=0&size=5&sort=nome,asc
GET /alunos?page=0&size=5&sort=nome,asc
GET /matriculas?page=0&size=5&sort=dataMatricula,desc
```

## Exemplos de uso

Envie os corpos abaixo como JSON, com o cabeçalho `Content-Type: application/json`. Os IDs são gerados pelo banco. Use os IDs retornados nos cadastros nas requisições seguintes.

### 1. Cadastrar curso: POST /cursos

```json
{
  "nome": "Programação Java",
  "cargaHoraria": 60,
  "categoria": "Tecnologia",
  "ativo": true
}
```

Para atualizar, envie os mesmos campos para `PUT /cursos/1`.

### 2. Cadastrar aluno: POST /alunos

```json
{
  "nome": "Ana Silva",
  "email": "ana@example.com",
  "idade": 18,
  "telefone": "(44) 99999-9999"
}
```

O telefone é opcional. Para atualizar, envie os dados para `PUT /alunos/1`.

### 3. Realizar matrícula: POST /matriculas

```json
{
  "dataMatricula": "2026-10-30",
  "status": "ATIVA",
  "alunoId": 1,
  "cursoId": 1
}
```

A resposta inclui a matrícula e os dados do aluno e do curso relacionados.

### 4. Atualizar status: PUT /matriculas/1

```json
{
  "status": "CONCLUIDA"
}
```

O status é uma String obrigatória, sem uma lista fixa de valores.

### 5. Cancelar matrícula: DELETE /matriculas/1

Não envie corpo. A matrícula será excluída. Depois de remover todas as matrículas relacionadas, é possível excluir o curso e o aluno.

## Validações e regras

- Curso: nome obrigatório com pelo menos 5 caracteres, carga horária obrigatória e positiva, categoria e indicação de ativo obrigatórias.
- Aluno: nome, email válido e idade obrigatórios; idade mínima de 16 anos; telefone opcional.
- Matrícula: data, status, aluno e curso obrigatórios.
- Não é permitido matricular em curso inativo nem matricular aluno menor de 16 anos.
- Não é permitido excluir curso ou aluno com matrículas, independentemente do status delas.

Respostas: `201 Created` no cadastro (com `Location`), `200 OK` em consultas e atualizações, `204 No Content` nas exclusões, `400 Bad Request` em dados inválidos ou matrícula não permitida, `404 Not Found` para ID inexistente e `409 Conflict` na exclusão de registros com matrículas.

Exemplo de erro de validação:

```json
{
  "idade": "O aluno deve ter pelo menos 16 anos."
}
```

Exemplo de erro de regra de negócio:

```json
{
  "erro": "Não é possível realizar matrícula em curso inativo."
}
```
