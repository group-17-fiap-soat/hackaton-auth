package hackaton.fiapx.commons.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta de autenticação com token JWT")
data class AuthUserResponseV1(
    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,

    @Schema(description = "Tipo do token", example = "Bearer")
    val tokenType: String = "Bearer"
)