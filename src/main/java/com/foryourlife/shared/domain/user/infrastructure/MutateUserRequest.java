package com.foryourlife.shared.domain.user.infrastructure;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;

public class MutateUserRequest {
    @NotNull(message = "El tipo no puede ser nulo")
    @NotEmpty(message = "El tipo no puede ser vacio")
    @Pattern(
            regexp = "^(S|V|ML)$",
            message = "El tipo solo puede ser S, V o ML"
    )
    public String type;

    @NotNull(message = "El id no puede ser nulo")
    @NotEmpty(message = "El id no puede ser vacio")
    @UUID(message = "El id no es valido")
    public String id;
}
