package ru.guybydefault.foody.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.guybydefault.foody.domain.Product

@Repository
interface ProductRepository : CrudRepository<Product, Int> {

    @Query("SELECT p FROM Product p WHERE (p.user IS NULL OR p.user.id = :userId) AND p.name LIKE CONCAT('%',:nameLike,'%')")
    fun findGlobalOrUserProductsByName(
        @Param("nameLike") nameLike: String,
        @Param("userId") userId: Int
    ): List<Product>

    fun findProductsByUserId(userId: Int): List<Product>
}