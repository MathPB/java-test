package java_test.domain.product.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java_test.domain.product.dto.WarehouseDto

data class InventoryRequestDto(
    @JsonProperty("warehouses")
    val warehouses: List<WarehouseDto> = listOf()
)
