package hackaton.fiapx.commons.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Schema(description = "Dados para registro de novo usuário")
data class RegisterUserRequestV1(
    @Schema(description = "Nome completo do usuário", example = "João Silva", required = true)
    @NotNull(message = "O campo não pode ser nulo.")
    var name: String? = null,

    @Schema(description = "E-mail do usuário", example = "joao@email.com", required = true)
    @Email(message = "O e-mail precisa ser válido.")
    var email: String? = null,

    @Schema(description = "Senha do usuário", example = "MinhaSenh@123", required = true)
    @NotNull(message = "O campo não pode ser nulo.")
    var pass: String? = null
)
