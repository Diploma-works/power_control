package ru.guybydefault.foody.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.guybydefault.foody.domain.Product
import ru.guybydefault.foody.service.ProductService

@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping
    fun searchProducts(
        @RequestParam("nameLike", required = false) searchText: String?,
        @RequestParam("user", required = true) userId: Int
    ): Iterable<Product> {
        if (searchText.isNullOrBlank()) {
            return productService.getUserProducts(userId)
        } else {
            return productService.searchByNameStartsWithOrContains(searchText, userId)
        }
    }
}