package ru.guybydefault.foody.parser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.repository.DiaryPositionRepository
import ru.guybydefault.foody.repository.ProductRepository
import ru.guybydefault.foody.repository.UserRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Component
@Transactional
class DBTestFiller {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var diaryPositionRepository: DiaryPositionRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var userRepository: UserRepository


    fun fill() {
        val product = productRepository.findAll().iterator().next()
        val user = userRepository.findAll().iterator().next()!!

        var currDate = Instant.now()
        var dishedPerLunch = 0
        for (i in 0..15000) {
            var diaryPosition = DiaryPosition(product.name, currDate, product.nutrientsInfo, 100.0, user)
            if (dishedPerLunch < 4) {
                currDate = currDate.plus(5, ChronoUnit.MINUTES)
                dishedPerLunch++
            } else {
                currDate = currDate.plus(4, ChronoUnit.HOURS)
                dishedPerLunch = 0
            }

            entityManager.persist(diaryPosition)

        }
    }

}

@Component
@Order(0)
class DBTestFillerStart() : ApplicationListener<ApplicationReadyEvent> {

    @Value(value = "#{new Boolean('\${fill.diary}'.trim())}")
    private var fillDiary: Boolean = false

    @Autowired
    private lateinit var dbTestFiller: DBTestFiller
    override fun onApplicationEvent(p0: ApplicationReadyEvent) {
        if (fillDiary) {
            dbTestFiller.fill()
        }
    }
}
