package ru.guybydefault.foody

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class FoodyApplication

fun main(args: Array<String>) {
	runApplication<FoodyApplication>(*args)
}
