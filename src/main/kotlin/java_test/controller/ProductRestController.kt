package java_test.controller

import java_test.domain.product.ProductServiceImpl
import java_test.domain.product.dto.request.ProductRequestDto
import java_test.domain.product.dto.response.ProductResponseDto
import org.springframework.web.bind.annotation.*

@RestController
class ProductRestController(
    val service: ProductServiceImpl
) {

    @PostMapping("/product")
    fun createProduct(
        @RequestBody request: ProductRequestDto
    ): ProductResponseDto {
        return service.createProduct(request)
    }

    @GetMapping("/product/{sku}")
    fun getProductBySku(@PathVariable sku: Int): ProductResponseDto {
        return service.getProductSku(sku)
    }

    @PutMapping("/product/{sku}")
    fun updateProductBySku(
        @PathVariable sku: Int,
        @RequestBody request: ProductRequestDto
    ): ProductResponseDto {
        return service.updateProductSku(sku, request)
    }

    @DeleteMapping("/product/{sku}")
    fun deleteProductBySku(@PathVariable sku: Int): Boolean {
        return service.deleteProductSku(sku)
    }
}