package ru.guybydefault.foody.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Recipe(
    var name: String,
    @OneToMany(mappedBy = "recipe", cascade = arrayOf(CascadeType.ALL))
    var tags: List<RecipeTag>,
    @OneToMany(mappedBy = "recipe", cascade = arrayOf(CascadeType.ALL))
    var products: List<RecipeProduct>,
    @ManyToOne(cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH))
    var nutrientsInfo: NutrientsInfo,
    var imageLink: String,
    /**
     * Minutes
     */
    var minutesPrepared: Int,
    var description: String,
    @ManyToOne
    var user: User?
) : BaseEntity() {
}