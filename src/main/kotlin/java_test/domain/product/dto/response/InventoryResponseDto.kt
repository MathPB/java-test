package java_test.domain.product.dto.response

import java_test.domain.product.dto.WarehouseDto

data class InventoryResponseDto(
    val quantity: Int,
    val warehouses: List<WarehouseDto>
)
