package utfpr.api_cursos.dtos;

import jakarta.validation.constraints.NotBlank;

public record StatusMatriculaDTO(
        @NotBlank(message = "O status é obrigatório.") String status
) {}
