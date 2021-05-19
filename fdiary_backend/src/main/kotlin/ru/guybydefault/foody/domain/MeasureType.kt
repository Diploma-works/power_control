package ru.guybydefault.foody.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table
data class MeasureType(
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    /**
     * How many grams in one unit described by this MeasureType
     */
    val grams: Double
) : BaseEntity() {
}