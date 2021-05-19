package ru.guybydefault.foody.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.guybydefault.foody.domain.MeasureType
@Repository
interface MeasureTypeRepository : CrudRepository<MeasureType, Int> {

}