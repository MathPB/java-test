package java_test.domain.product

import java_test.domain.product.dto.WarehouseDto
import java_test.domain.product.dto.request.ProductRequestDto
import java_test.domain.product.dto.response.ProductResponseDto
import java_test.domain.product.mappers.ProductMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductServiceImpl(
    private val repository: InMemoryProductRepository
) {
    private val log: Logger = LogManager.getLogger()

    fun createProduct(request: ProductRequestDto): ProductResponseDto {
        log.info("f=createProduct(), creating product, request=$request")

        if (repository.findBySku(request.sku) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "f=createProduct(), " +
                    "Product already exists, sku=${request.sku}")
        }

        val (warehouseList, inventoryQuantity, isMarketable) = buildProductResponse(request)

        return repository.save(
            ProductMapper.toProductResponseDto(
                request, warehouseList, inventoryQuantity, isMarketable
            )
        )
    }

    fun getProductSku(sku: Int): ProductResponseDto {
        log.info("f=getProductSku(), getting product, sku=$sku")

        return repository.findBySku(sku) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found, sku=$sku!"
        )
    }

    fun updateProductSku(sku: Int, request: ProductRequestDto): ProductResponseDto {
        log.info("f=updateProductSku(), updating product, sku=$sku, request=$request")

        val product = repository.findBySku(sku) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found, sku=$sku!"
        )

        val (warehouseList, inventoryQuantity, isMarketable) = buildProductResponse(request)

        return repository.save(
            ProductMapper.toProductResponseDto(
                product,
                warehouseList,
                inventoryQuantity,
                isMarketable
            )
        )
    }

    fun deleteProductSku(sku: Int): Boolean {
        log.info("f=deleteProductSku(), deleting product, sku=$sku")

        repository.findBySku(sku) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Product not found, sku=$sku!"
        )

        repository.delete(sku)

        return true
    }

    private fun buildProductResponse(request: ProductRequestDto): Triple<List<WarehouseDto>, Int, Boolean> {
        val warehouseList = request.inventory.warehouses.map {
            WarehouseDto(
                locality = it.locality,
                quantity = it.quantity,
                type = it.type
            )
        }
        val inventoryQuantity = request.inventory.warehouses.sumOf { it.quantity }
        val isMarketable = inventoryQuantity > 0

        return Triple(warehouseList, inventoryQuantity, isMarketable)
    }
}