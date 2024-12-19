package java_test.domain.product.mappers

import java_test.domain.product.dto.WarehouseDto
import java_test.domain.product.dto.request.ProductRequestDto
import java_test.domain.product.dto.response.InventoryResponseDto
import java_test.domain.product.dto.response.ProductResponseDto

object ProductMapper {

    fun toProductResponseDto(
        product: ProductRequestDto,
        warehouseList: List<WarehouseDto>,
        inventoryQuantity: Int,
        isMarketable: Boolean
    ): ProductResponseDto {
        return ProductResponseDto(
            sku = product.sku,
            name = product.name,
            inventory = InventoryResponseDto(
                quantity = inventoryQuantity,
                warehouses = warehouseList
            ),
            isMarketable = isMarketable
        )
    }

    fun toProductResponseDto(
        product: ProductResponseDto,
        warehouseList: List<WarehouseDto>,
        inventoryQuantity: Int,
        isMarketable: Boolean
    ): ProductResponseDto {
        return ProductResponseDto(
            sku = product.sku,
            name = product.name,
            inventory = InventoryResponseDto(
                quantity = inventoryQuantity,
                warehouses = warehouseList
            ),
            isMarketable = isMarketable
        )
    }
}