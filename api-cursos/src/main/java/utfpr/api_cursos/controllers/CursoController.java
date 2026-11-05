package utfpr.api_cursos.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utfpr.api_cursos.models.Curso;
import utfpr.api_cursos.repositories.CursoRepository;
import utfpr.api_cursos.repositories.MatriculaRepository;
import java.net.URI;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final CursoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public CursoController(CursoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    @PostMapping
    public ResponseEntity<Curso> cadastrar(@Valid @RequestBody Curso curso) {
        Curso salvo = repository.save(curso);
        return ResponseEntity.created(URI.create("/cursos/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public Page<Curso> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Curso buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));
    }

    @PutMapping("/{id}")
    public Curso atualizar(@PathVariable Long id, @Valid @RequestBody Curso dados) {
        Curso curso = buscar(id);
        curso.setNome(dados.getNome());
        curso.setCargaHoraria(dados.getCargaHoraria());
        curso.setCategoria(dados.getCategoria());
        curso.setAtivo(dados.getAtivo());
        return repository.save(curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Curso curso = buscar(id);
        if (matriculaRepository.existsByCursoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir curso que possui matrículas.");
        }
        repository.delete(curso);
        return ResponseEntity.noContent().build();
    }
}
