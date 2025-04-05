package com.apibanking.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.apibanking.dto.ApiResponse;
import com.apibanking.product.entity.Product;
import com.apibanking.product.service.ProductService;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class ProductResourceTest {

	// Inject mocked ProductService
	@InjectMock
	ProductService productService; // This should be mocked

	@Inject
	ProductResource productResource; // Inject the resource (no need to mock this)

	@Test
	public void testCreateProduct() {
		// Setup the mock behavior
		Product mockProduct = new Product();
		mockProduct.setName("Test Product");

		// Mock the service method
		when(productService.create(any(Product.class))).thenReturn(Uni.createFrom().item(mockProduct));

		// Call the resource method
		Response response = productResource.createProduct(mockProduct).await().indefinitely();

		// Assert the response
		assertEquals(201, response.getStatus());
		assertNotNull(response.getEntity());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product created successfully", apiResponse.getMessage());
	}

	@Test
	public void testGetAllProducts() {
		// Setup mock data
		Product product1 = new Product();
		product1.setName("Product 1");

		Product product2 = new Product();
		product2.setName("Product 2");

		// Mock the service method
		when(productService.getAll()).thenReturn(Uni.createFrom().item(List.of(product1, product2)));

		// Call the resource method
		Response response = productResource.getAllProducts().await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertTrue(((List<?>) apiResponse.getData()).size() > 0);
	}

	@Test
	public void testGetProductById() {
		// Setup mock data
		Product mockProduct = new Product();
		mockProduct.setName("Test Product");

		// Mock the service method
		when(productService.getById(anyInt())).thenReturn(Uni.createFrom().item(Optional.of(mockProduct)));

		// Call the resource method
		Response response = productResource.getProductById(1).await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product found", apiResponse.getMessage());
	}

	@Test
	public void testUpdateProduct() {
		// Setup mock data
		Product updatedProduct = new Product();
		updatedProduct.setName("Updated Product");

		// Mock the service method
		when(productService.update(anyInt(), any(Product.class))).thenReturn(Uni.createFrom().item(updatedProduct));

		// Call the resource method
		Response response = productResource.updateProduct(1, updatedProduct).await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product updated successfully", apiResponse.getMessage());
	}

	@Test
	public void testDeleteProduct() {
		// Setup the mock behavior
		when(productService.delete(anyInt())).thenReturn(Uni.createFrom().voidItem());

		// Call the resource method
		Response response = productResource.deleteProduct(1).await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product deleted successfully", apiResponse.getMessage());
	}

	@Test
	public void testCheckStock() {
		// Mock the service method
		when(productService.isStockAvailable(anyInt(), anyInt())).thenReturn(Uni.createFrom().item(true));

		// Call the resource method
		Response response = productResource.checkStock(1, 10).await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Stock is available", apiResponse.getMessage());
	}

	@Test
	public void testGetProductsSortedByPrice() {
		// Setup mock data
		Product product1 = new Product();
		product1.setName("Product 1");
		product1.setPrice(1000.0);

		Product product2 = new Product();
		product2.setName("Product 2");
		product2.setPrice(1000.0);

		// Mock the service method
		when(productService.getSortedByPrice()).thenReturn(Uni.createFrom().item(List.of(product2, product1)));

		// Call the resource method
		Response response = productResource.getProductsSortedByPrice().await().indefinitely();

		// Assert the response
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		List<?> sortedList = (List<?>) apiResponse.getData();
		assertEquals("Product 2", ((Product) sortedList.get(0)).getName());
		assertEquals("Product 1", ((Product) sortedList.get(1)).getName());
	}
}
