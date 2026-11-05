package utfpr.api_cursos.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utfpr.api_cursos.dtos.MatriculaRequestDTO;
import utfpr.api_cursos.dtos.StatusMatriculaDTO;
import utfpr.api_cursos.models.Aluno;
import utfpr.api_cursos.models.Curso;
import utfpr.api_cursos.models.Matricula;
import utfpr.api_cursos.repositories.AlunoRepository;
import utfpr.api_cursos.repositories.CursoRepository;
import utfpr.api_cursos.repositories.MatriculaRepository;
import java.net.URI;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {
    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public MatriculaController(MatriculaRepository repository, AlunoRepository alunoRepository,
                               CursoRepository cursoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Matricula> cadastrar(@Valid @RequestBody MatriculaRequestDTO dados) {
        Aluno aluno = alunoRepository.findById(dados.alunoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));
        Curso curso = cursoRepository.findById(dados.cursoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));

        if (!Boolean.TRUE.equals(curso.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível realizar matrícula em curso inativo.");
        }
        if (aluno.getIdade() == null || aluno.getIdade() < 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O aluno deve ter pelo menos 16 anos para se matricular.");
        }

        Matricula matricula = new Matricula();
        matricula.setDataMatricula(dados.dataMatricula());
        matricula.setStatus(dados.status());
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        Matricula salva = repository.save(matricula);
        return ResponseEntity.created(URI.create("/matriculas/" + salva.getId())).body(salva);
    }

    @GetMapping
    public Page<Matricula> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Matricula buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada."));
    }

    @PutMapping("/{id}")
    public Matricula atualizarStatus(@PathVariable Long id,
                                    @Valid @RequestBody StatusMatriculaDTO dados) {
        Matricula matricula = buscar(id);
        matricula.setStatus(dados.status());
        return repository.save(matricula);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }
}
