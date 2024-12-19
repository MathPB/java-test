package java_test.domain.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java_test.domain.product.enums.WarehouseTypeEnum

data class WarehouseDto(
    @JsonProperty("locality")
    val locality: String,
    @JsonProperty("quantity")
    val quantity: Int,
    @JsonProperty("type")
    val type: WarehouseTypeEnum
)
