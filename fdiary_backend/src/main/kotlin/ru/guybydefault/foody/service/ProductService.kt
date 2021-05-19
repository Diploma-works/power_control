package ru.guybydefault.foody.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.guybydefault.foody.domain.NutrientsInfo
import ru.guybydefault.foody.domain.Product
import ru.guybydefault.foody.domain.User
import ru.guybydefault.foody.dtos.ProductDto
import ru.guybydefault.foody.repository.ProductRepository

@Service
class ProductService {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var nutrientService: NutrientService

    fun createUserProduct(name: String, nutrientsInfo: NutrientsInfo, user: User) {
        TODO("Not yet implemented")
    }

    fun createProduct(name: String, nutrientsInfo: NutrientsInfo) {
        TODO("Not yet implemented")
    }

    fun deleteUserProduct(id: Int): Boolean {
        val product = productRepository.findById(id)
        if (product.isPresent) {
            productRepository.delete(product.get())
            return true
        } else {
            return false
        }
    }

    fun convertToProduct(productDto: ProductDto) : Product {
        return Product(
            productDto.name,
            nutrientService.convertToNutrient(productDto.nutrientsInfo),
            productDto.dateCreated,
            productDto.dateUpdated,
            productDto.user
        )
    }

    fun searchByNameStartsWithOrContains(substring: String, userId: Int): List<Product> {
        // find all products which do not belong to any user PLUS custom products of the user
        return productRepository.findGlobalOrUserProductsByName(substring, userId)
    }

    fun getUserProducts(userId: Int): List<Product> {
        return productRepository.findProductsByUserId(userId)
    }

    fun findById(id: Int): Product? {
        return productRepository.findById(id).orElse(null)
    }
}