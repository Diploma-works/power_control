package ru.guybydefault.foody.domain

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "\"user\"")
data class User(
    var login: String?,
    var password: String?
) : BaseEntity() {
}