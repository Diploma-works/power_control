package ru.guybydefault.foody.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
data class RecipeTag(
    val name: String,
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true) // otherwise first ref as POJO, others as id
    val recipe: Recipe
): BaseEntity() {
}