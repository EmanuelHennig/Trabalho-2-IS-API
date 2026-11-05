package utfpr.api_cursos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MatriculaRequestDTO(
        @NotNull(message = "A data da matrícula é obrigatória.") LocalDate dataMatricula,
        @NotBlank(message = "O status é obrigatório.") String status,
        @NotNull(message = "O aluno é obrigatório.") Long alunoId,
        @NotNull(message = "O curso é obrigatório.") Long cursoId
) {}
