package utfpr.api_cursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import utfpr.api_cursos.models.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
