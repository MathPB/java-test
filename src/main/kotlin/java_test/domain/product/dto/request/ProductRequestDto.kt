package java_test.domain.product.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductRequestDto(
    @JsonProperty("sku")
    val sku: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("inventory")
    val inventory: InventoryRequestDto,
)
