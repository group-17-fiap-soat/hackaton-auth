package hackaton.fiapx.adapters.controllers

import hackaton.fiapx.adapters.presenters.UserMapper
import hackaton.fiapx.commons.config.jwt.JwtService
import hackaton.fiapx.commons.dto.request.AuthUserRequestV1
import hackaton.fiapx.commons.dto.request.RegisterUserRequestV1
import hackaton.fiapx.commons.dto.response.AuthUserResponseV1
import hackaton.fiapx.commons.exception.UserAlreadyExistsException
import hackaton.fiapx.usecases.auth.LoginUserUseCase
import hackaton.fiapx.usecases.auth.RegisterUserUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import hackaton.fiapx.adapters.controllers.operation.AuthOperation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder

) : AuthOperation {
    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário no sistema")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            ApiResponse(
                responseCode = "400", description = "Usuário já existe ou dados inválidos",
                content = [Content(schema = Schema(implementation = String::class))]
            )
        ]
    )
    override fun register(@RequestBody request: RegisterUserRequestV1): ResponseEntity<String> {
        return try {
            val encodedPassword = passwordEncoder.encode(request.pass)
            val requestWithEncodedPassword = request.copy(pass = encodedPassword)

            registerUserUseCase.execute(
                UserMapper.fromRequestToDomain(requestWithEncodedPassword)
            )
            ResponseEntity.ok("Usuário registrado com sucesso")
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Login realizado com sucesso",
                content = [Content(schema = Schema(implementation = AuthUserResponseV1::class))]
            ),
            ApiResponse(
                responseCode = "401", description = "Credenciais inválidas",
                content = [Content(schema = Schema(implementation = String::class))]
            )
        ]
    )
    override fun login(@RequestBody request: AuthUserRequestV1): ResponseEntity<Any> {
        return try {
            val user = loginUserUseCase.execute(UserMapper.fromRequestToDomain(request))

            val userDetails = User.builder()
                .username(user?.email)
                .password(user?.passwordHash ?: "")
                .roles("USER")
                .build()

            val extraClaims = mapOf<String, Any>(
                "email" to (user?.email ?: ""),
                "name" to (user?.name ?: ""),
                "userId" to (user?.id?.toString() ?: "")
            )
            val token = jwtService.generateToken(extraClaims, userDetails)
            ResponseEntity.ok(AuthUserResponseV1(token))
        } catch (_: AuthenticationException) {
            ResponseEntity.status(401).body("Credenciais inválidas")
        }
    }
}