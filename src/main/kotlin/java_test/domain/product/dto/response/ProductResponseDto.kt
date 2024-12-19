package java_test.domain.product.dto.response


data class ProductResponseDto(
    val sku: Int,
    val name: String,
    val inventory: InventoryResponseDto,
    val isMarketable: Boolean
)
