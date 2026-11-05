package utfpr.api_cursos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "A data da matrícula é obrigatória.")
    private LocalDate dataMatricula;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @NotNull(message = "O aluno é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull(message = "O curso é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

}
