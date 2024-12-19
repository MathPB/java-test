package java_test.domain.product

import java_test.domain.product.dto.WarehouseDto
import java_test.domain.product.dto.request.InventoryRequestDto
import java_test.domain.product.dto.request.ProductRequestDto
import java_test.domain.product.dto.response.InventoryResponseDto
import java_test.domain.product.dto.response.ProductResponseDto
import java_test.domain.product.enums.WarehouseTypeEnum
import java_test.domain.product.mappers.ProductMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import org.springframework.web.server.ResponseStatusException

class ProductServiceImplTest {

    private lateinit var repository: InMemoryProductRepository
    private lateinit var service: ProductServiceImpl

    @BeforeEach
    fun setUp() {
        repository = mock(InMemoryProductRepository::class.java)
        service = ProductServiceImpl(repository)
    }

    @Test
    fun `should create product successfully`() {
        val productRequest = ProductRequestDto(
            sku = 123,
            name = "Product 123",
            inventory = InventoryRequestDto(
                warehouses = listOf(
                    WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE),
                    WarehouseDto("RJ", 5, WarehouseTypeEnum.PHYSICAL_STORE)
                )
            )
        )

        BDDMockito.given(repository.findBySku(123)).willReturn(null)
        BDDMockito.given(repository.save(ProductMapper.toProductResponseDto(
            productRequest,
            productRequest.inventory.warehouses,
            15,
            true
        ))).willReturn(
            ProductResponseDto(
                123,
                "Product 123",
                InventoryResponseDto(
                    quantity = 15,
                    warehouses = listOf(
                        WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE),
                        WarehouseDto("RJ", 5, WarehouseTypeEnum.PHYSICAL_STORE)
                    )
                ),
                true
            )
        )

        val result = service.createProduct(productRequest)

        assertEquals("Product 123", result.name)
        assertEquals(15, result.inventory.quantity)
        assertEquals(2, result.inventory.warehouses.size)
        assertTrue(result.isMarketable)
    }

    @Test
    fun `should throw conflict exception when creating product with existing sku`() {
        val productRequest = ProductRequestDto(
            sku = 123,
            name = "Product 123",
            inventory = InventoryRequestDto(
                warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
            )
        )

        BDDMockito.given(repository.findBySku(123)).willReturn(
            ProductResponseDto(
                123,
                "Product 123",
                InventoryResponseDto(
                    quantity = 10,
                    warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
                ),
                true
            )
        )

        val exception = assertThrows<ResponseStatusException> {
            service.createProduct(productRequest)
        }

        assertEquals("f=createProduct(), Product already exists, sku=123", exception.reason)
        assertEquals(409, exception.statusCode.value())
    }

    @Test
    fun `should return product by sku`() {
        val sku = 123
        val productResponse = ProductResponseDto(
            123,
            "Product 123",
            InventoryResponseDto(
                quantity = 10,
                warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
            ),
            true
        )

        BDDMockito.given(repository.findBySku(sku)).willReturn(productResponse)

        val result = service.getProductSku(sku)

        assertEquals(sku, result.sku)
        assertEquals("Product 123", result.name)
        assertEquals(10, result.inventory.quantity)
        assertTrue(result.isMarketable)
    }

    @Test
    fun `should throw not found exception when product does not exist`() {
        val sku = 999

        BDDMockito.given(repository.findBySku(sku)).willReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            service.getProductSku(sku)
        }

        assertEquals("Product not found, sku=999!", exception.reason)
        assertEquals(404, exception.statusCode.value())
    }

    @Test
    fun `should update product by sku`() {
        val sku = 123
        val productRequest = ProductRequestDto(
            sku = 123,
            name = "Product 123",
            inventory = InventoryRequestDto(
                warehouses = listOf(
                    WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE),
                    WarehouseDto("RJ", 5, WarehouseTypeEnum.PHYSICAL_STORE)
                )
            )
        )

        val productResponse = ProductResponseDto(
            123,
            "Product 123",
            InventoryResponseDto(
                quantity = 10,
                warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
            ),
            true
        )

        BDDMockito.given(repository.findBySku(sku)).willReturn(productResponse)
        BDDMockito.given(repository.save(ProductMapper.toProductResponseDto(
            productResponse,
            productRequest.inventory.warehouses,
            15,
            true
        ))).willReturn(
            ProductResponseDto(
                123,
                "Product 123",
                InventoryResponseDto(
                    quantity = 15,
                    warehouses = listOf(
                        WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE),
                        WarehouseDto("RJ", 5, WarehouseTypeEnum.PHYSICAL_STORE)
                    )
                ),
                true
            )
        )

        val result = service.updateProductSku(sku, productRequest)

        assertEquals(sku, result.sku)
        assertEquals("Product 123", result.name)
        assertEquals(15, result.inventory.quantity)
        assertEquals(2, result.inventory.warehouses.size)
        assertTrue(result.isMarketable)
    }

    @Test
    fun `should throw not found exception when updating product that does not exist`() {
        val sku = 999
        val productRequest = ProductRequestDto(
            sku = 999,
            name = "Product 999",
            inventory = InventoryRequestDto(
                warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
            )
        )

        BDDMockito.given(repository.findBySku(sku)).willReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            service.updateProductSku(sku, productRequest)
        }

        assertEquals("Product not found, sku=999!", exception.reason)
        assertEquals(404, exception.statusCode.value())
    }

    @Test
    fun `should delete product by sku`() {
        val sku = 123
        val productResponse = ProductResponseDto(
            123,
            "Product 123",
            InventoryResponseDto(
                quantity = 10,
                warehouses = listOf(WarehouseDto("SP", 10, WarehouseTypeEnum.ECOMMERCE))
            ),
            true
        )

        BDDMockito.given(repository.findBySku(sku)).willReturn(productResponse)
        BDDMockito.given(repository.delete(sku)).willReturn(true)

        val result = service.deleteProductSku(sku)

        assertTrue(result)
    }

    @Test
    fun `should throw not found exception when deleting product that does not exist`() {
        val sku = 999

        BDDMockito.given(repository.findBySku(sku)).willReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            service.deleteProductSku(sku)
        }

        assertEquals("Product not found, sku=999!", exception.reason)
        assertEquals(404, exception.statusCode.value())
    }
}
