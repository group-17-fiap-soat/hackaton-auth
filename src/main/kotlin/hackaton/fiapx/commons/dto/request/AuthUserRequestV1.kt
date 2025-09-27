package hackaton.fiapx.commons.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Schema(description = "Dados para autenticação do usuário")
data class AuthUserRequestV1(
    @Schema(description = "E-mail do usuário", example = "usuario@email.com", required = true)
    @Email(message = "O e-mail precisa ser válido.")
    var email: String? = null,

    @Schema(description = "Senha do usuário", example = "MinhaSenh@123", required = true)
    @NotNull(message = "O campo não pode ser nulo.")
    var pass: String? = null
)
