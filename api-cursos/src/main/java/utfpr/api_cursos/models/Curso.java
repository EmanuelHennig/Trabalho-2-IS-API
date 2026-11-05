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
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 5, message = "O nome deve ter no mínimo 5 caracteres.")
    private String nome;

    @NotNull(message = "A carga horária é obrigatória.")
    @Positive(message = "A carga horária deve ser maior que zero.")
    private Integer cargaHoraria;

    @NotBlank(message = "A categoria é obrigatória.")
    private String categoria;

    @NotNull(message = "Informe se o curso está ativo.")
    private Boolean ativo;

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<Matricula> matriculas = new ArrayList<>();

}
