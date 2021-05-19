package ru.guybydefault.foody.domain

import javax.persistence.*

@Entity
@Table
/**
 * This entity should be immutable (never changed)
 */
data class NutrientsInfo(
    @OneToMany(mappedBy = "nutrientsInfo", cascade = [CascadeType.ALL])
    val nutrients: List<Nutrient>,
    @Column(name = "calories")
    val calories: Double,
    @Column(name = "protein")
    val protein: Double,
    @Column(name = "fat")
    val fat: Double,
    @Column(name = "carb")
    val carb: Double
) : BaseEntity() {
}