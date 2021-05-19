package ru.guybydefault.foody.service

import ru.guybydefault.foody.domain.Nutrient

interface INutrientService {
    fun addNutrient(nutrient: Nutrient): Nutrient

    /**
     * Admin only function
     */
    fun removeNutrient(nutrientId: Int): Boolean
}