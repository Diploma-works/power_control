package ru.guybydefault.foody.parser

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component

@Component
class HealthDietLoader : ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private lateinit var healthDietParser: HealthDietParser


    @Value(value = "#{new Boolean('\${initdb}'.trim())}")
    private var init: Boolean = false

    fun parse() {
        try {
            healthDietParser.parseNutrientTypesAndMeta()
        } catch (e: DataIntegrityViolationException) {

        }
        healthDietParser.parseData()
    }

    override fun onApplicationEvent(p0: ApplicationReadyEvent) {
        if (init) {
            parse()
        }
    }
}