package utfpr.api_cursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import utfpr.api_cursos.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
