package ru.guybydefault.foody.domain

import javax.persistence.*

@Entity
@Table
data class NutrientType(
    @Column(nullable = false)
    val name: String,
    @ManyToOne(optional = false)
    @JoinColumn(name = "default_measure_type", referencedColumnName = "id")
    val defaultMeasureType: MeasureType
) : BaseEntity(){
}