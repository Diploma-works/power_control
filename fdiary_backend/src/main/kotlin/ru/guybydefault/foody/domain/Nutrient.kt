package ru.guybydefault.foody.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator
import javax.persistence.*

@Entity
@Table(name = "nutrient")
data class Nutrient(
    @ManyToOne(optional = false)
    @JoinColumn(name = "nutrient_type", referencedColumnName = "id")
    @JsonIdentityInfo(generator = PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true) // otherwise first ref as POJO, others as id
    val nutrientType: NutrientType,
    @Column(nullable = false)
    val value: Double,
    @ManyToOne(optional = false)
    @JoinColumn(name = "nutrients_info", referencedColumnName = "id")
    @JsonIdentityInfo(generator = PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true) // otherwise first ref as POJO, others as id
    val nutrientsInfo: NutrientsInfo
) : BaseEntity() {
    override fun toString(): String {
        return nutrientType.toString() + ": " + value + " nutrientsInfo ID " + nutrientsInfo.id
    }
}