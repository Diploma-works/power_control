package ru.guybydefault.foody.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.guybydefault.foody.domain.User
import ru.guybydefault.foody.dtos.AuthRequest
import ru.guybydefault.foody.dtos.AuthResponse
import ru.guybydefault.foody.dtos.RegistrationRequest
import ru.guybydefault.foody.service.UserService
import ru.guybydefault.foody.service.auth.jwt.JwtProvider
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @PostMapping("register")
    fun registerUser(@RequestBody @Valid registrationRequest: RegistrationRequest): ResponseEntity<Any> {
        val user = userService.register(registrationRequest.login!!, registrationRequest.password!!)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("login")
    fun auth(@RequestBody @Valid request: AuthRequest): ResponseEntity<AuthResponse> {
        val user = userService.findByLoginAndPassword(request.login!!, request.password!!)
        if (user == null) {
            return ResponseEntity.notFound().build()
        }
        val token = jwtProvider!!.generateToken(user.login)
        return ResponseEntity.ok(AuthResponse(user.login!!, token))
    }

    @GetMapping("token")
    fun getNewToken(@ModelAttribute("user") user: User): ResponseEntity<AuthResponse> {
        val token = jwtProvider!!.generateToken(user.login)
        return ResponseEntity.ok(AuthResponse(user.login!!, token))
    }
}