package utfpr.api_cursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import utfpr.api_cursos.models.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByCursoId(Long cursoId);
    boolean existsByAlunoId(Long alunoId);
}
