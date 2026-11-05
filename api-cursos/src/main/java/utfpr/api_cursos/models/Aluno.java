package utfpr.api_cursos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Informe um email válido.")
    private String email;

    @NotNull(message = "A idade é obrigatória.")
    @Min(value = 16, message = "O aluno deve ter pelo menos 16 anos.")
    private Integer idade;

    private String telefone;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<Matricula> matriculas = new ArrayList<>();

}
