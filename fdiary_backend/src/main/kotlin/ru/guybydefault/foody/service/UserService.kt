package ru.guybydefault.foody.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.guybydefault.foody.domain.User
import ru.guybydefault.foody.repository.UserRepository

@Service
@Transactional
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    fun findById(userId: Int): User? {
        return userRepository.findById(userId).orElse(null)
    }

    fun register(login: String, password: String): User? {
        if (findByLogin(login) == null) {
            val user = User(login, passwordEncoder.encode(password))
            return userRepository.save(user)
        } else {
            return null
        }
    }

    fun findByLogin(login: String): User? {
        return userRepository.findByLogin(login).orElse(null)
    }

    fun findByLoginAndPassword(login: String, password: String): User? {
        val user = findByLogin(login)
        if (user == null) {
            return null
        }
        if (passwordEncoder.matches(password, user.password)) {
            return user
        } else {
            return null
        }
    }
}
