package utfpr.api_cursos.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utfpr.api_cursos.models.Aluno;
import utfpr.api_cursos.repositories.AlunoRepository;
import utfpr.api_cursos.repositories.MatriculaRepository;
import java.net.URI;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public AlunoController(AlunoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrar(@Valid @RequestBody Aluno aluno) {
        Aluno salvo = repository.save(aluno);
        return ResponseEntity.created(URI.create("/alunos/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public Page<Aluno> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Aluno buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));
    }

    @PutMapping("/{id}")
    public Aluno atualizar(@PathVariable Long id, @Valid @RequestBody Aluno dados) {
        Aluno aluno = buscar(id);
        aluno.setNome(dados.getNome());
        aluno.setEmail(dados.getEmail());
        aluno.setIdade(dados.getIdade());
        aluno.setTelefone(dados.getTelefone());
        return repository.save(aluno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Aluno aluno = buscar(id);
        if (matriculaRepository.existsByAlunoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir aluno que possui matrículas.");
        }
        repository.delete(aluno);
        return ResponseEntity.noContent().build();
    }
}
