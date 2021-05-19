package ru.guybydefault.foody.service

import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.Product
import ru.guybydefault.foody.domain.User

interface IProductService {

    fun createUserProduct(name: String, nutrientsInfo: NutrientsInfo, user: User)

    /**
     * Admin only feature
     */
    fun createProduct(name: String, nutrientsInfo: NutrientsInfo)

    /**
     * Returns true if product has been successfully deleted
     */
    fun deleteUserProduct(id: Int): Boolean

    fun searchByNameStartsWithOrContains(substring: String, user: User) : List<Product>
}