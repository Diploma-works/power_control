package ru.guybydefault.foody.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.guybydefault.foody.domain.NutrientType
import ru.guybydefault.foody.repository.NutrientTypeRepository

@RequestMapping("/nutrients")
@RestController
class NutrientController {

    @Autowired
    private lateinit var nutrientTypeRepository: NutrientTypeRepository

    @GetMapping("/types")
    fun nutrientTypes(): Iterable<NutrientType> {
        return nutrientTypeRepository.findAll()
    }
}