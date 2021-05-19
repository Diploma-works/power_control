package ru.guybydefault.foody.domain


abstract class BaseEntity {
    var id: Int? = null

    val isNew: Boolean
        get() = id == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

}
