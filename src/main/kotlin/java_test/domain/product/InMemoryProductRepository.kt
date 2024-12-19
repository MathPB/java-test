package java_test.domain.product

import java_test.domain.product.dto.response.ProductResponseDto
import org.springframework.stereotype.Component

@Component
object InMemoryProductRepository {
    private val products = mutableMapOf<Int, ProductResponseDto>()

    fun save(product: ProductResponseDto): ProductResponseDto {
        products[product.sku] = product

        return product
    }

    fun findBySku(sku: Int): ProductResponseDto? {
        return products[sku]
    }

    fun delete(sku: Int): Boolean {
        return products.remove(sku) != null
    }
}