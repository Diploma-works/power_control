package ru.guybydefault.foody.domain

import java.time.Instant
import javax.persistence.*

@Entity
@Table
data class Product(
    var name: String,
    @ManyToOne(optional = false, cascade = arrayOf(CascadeType.PERSIST))
    @JoinColumn(name = "nutrients_info", referencedColumnName = "id")
    var nutrientsInfo: NutrientsInfo,
    var dateCreated: Instant,
    var dateUpdated: Instant,
    @ManyToOne(optional = true)
    @JoinColumn(name = "\"user\"", referencedColumnName = "id")
    val user: User?
) : BaseEntity() {
}