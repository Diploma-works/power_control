package ru.guybydefault.foody.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.service.DiaryService
import ru.guybydefault.foody.service.ProductService
import ru.guybydefault.foody.service.UserService
import java.time.Instant

@RestController
@RequestMapping("/diaryPositions")
class DiaryPositionController {

    @Autowired
    private lateinit var diaryService: DiaryService

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun getDiaryPositions(
        @RequestParam userId: Int,
        @RequestParam start: Instant,
        @RequestParam end: Instant
    ): List<DiaryPosition> {
        return diaryService.getDiaryPositions(userId, start, end)
    }

    @PostMapping("custom")
    fun saveDiaryPosition(@RequestBody diaryPosition: DiaryPosition): DiaryPosition {
        return diaryService.saveDiaryPosition(diaryPosition)
    }

    @PostMapping("fromProduct")
    fun saveDiaryPosition(
        @RequestParam name: String,
        @RequestParam productId: Int,
        @RequestParam date: Instant,
        @RequestParam userId: Int,
        @RequestParam value: Double
    ): ResponseEntity<Any> {
        val user = userService.findById(userId)
        if (user == null) {
            return ResponseEntity.notFound().build()
        }
        val product = productService.findById(productId)
        if (product == null) {
            return ResponseEntity.notFound().build()
        }
        val diaryPosition = DiaryPosition(name, date, product.nutrientsInfo, value, user)
        return ResponseEntity.ok(diaryService.saveDiaryPosition(diaryPosition))
    }

    @DeleteMapping("{id}")
    fun deleteDiaryPosition(@PathVariable("id") id: Int): ResponseEntity<Any> {
        val diaryPosition = diaryService.getDiaryPosition(id)
        if (diaryPosition == null) {
            return ResponseEntity.notFound().build<Any>()
        }
        diaryService.deleteDiaryPosition(id)
        return ResponseEntity.ok().build()
    }
}