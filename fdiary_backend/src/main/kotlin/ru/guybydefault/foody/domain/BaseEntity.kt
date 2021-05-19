package ru.guybydefault.foody.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass


@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "pooled")
    @GenericGenerator(name = "pooled", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = [Parameter(name = "value_column_name", value = "sequence_next_hi_value"), Parameter(name = "prefer_entity_table_as_segment_value", value = "true"), Parameter(name = "optimizer", value = "pooled-lo"), Parameter(name = "increment_size", value = "100")])
    var id: Int? = null

    val isNew: Boolean
        get() = id == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

}
