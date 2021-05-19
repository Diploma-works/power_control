package ru.guybydefault.foody.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.guybydefault.foody.domain.User
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Int> {

    @Transactional(readOnly = true)
    fun findByLogin(login: String): Optional<User>

}