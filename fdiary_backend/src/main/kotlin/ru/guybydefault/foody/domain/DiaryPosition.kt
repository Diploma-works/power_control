package ru.guybydefault.foody.domain

import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class DiaryPosition(
    val name : String,
    val date: Instant,
    @ManyToOne(cascade = arrayOf(CascadeType.PERSIST))
    @JoinColumn(name = "nutrients_info", referencedColumnName = "id")
    val nutrientInfo: NutrientsInfo,
    val value: Double,
    @ManyToOne()
    @JoinColumn(name = "\"user\"", referencedColumnName = "id")
    val user: User
) : BaseEntity() {
}