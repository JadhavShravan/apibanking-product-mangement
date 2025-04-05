package com.apibanking.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.apibanking.product.dto.ApiResponse;
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

	@InjectMock
	ProductService productService; 

	@Inject
	ProductResource productResource; 

	@Test
	public void testCreateProduct() {
		Product mockProduct = new Product();
		mockProduct.setName("Test Product");

		when(productService.create(any(Product.class))).thenReturn(Uni.createFrom().item(mockProduct));

		Response response = productResource.createProduct(mockProduct).await().indefinitely();

	
		assertEquals(201, response.getStatus());
		assertNotNull(response.getEntity());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product created successfully", apiResponse.getMessage());
	}

	@Test
	public void testGetAllProducts() {

		Product product1 = new Product();
		product1.setName("Product 1");
		Product product2 = new Product();
		product2.setName("Product 2");
		when(productService.getAll()).thenReturn(Uni.createFrom().item(List.of(product1, product2)));
		Response response = productResource.getAllProducts().await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertTrue(((List<?>) apiResponse.getData()).size() > 0);
	}

	@Test
	public void testGetProductById() {
		Product mockProduct = new Product();
		mockProduct.setName("Test Product");
		when(productService.getById(anyInt())).thenReturn(Uni.createFrom().item(Optional.of(mockProduct)));
		Response response = productResource.getProductById(1).await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product found", apiResponse.getMessage());
	}

	@Test
	public void testUpdateProduct() {
		Product updatedProduct = new Product();
		updatedProduct.setName("Updated Product");
		when(productService.update(anyInt(), any(Product.class))).thenReturn(Uni.createFrom().item(updatedProduct));
		Response response = productResource.updateProduct(1, updatedProduct).await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product updated successfully", apiResponse.getMessage());
	}

	@Test
	public void testDeleteProduct() {
		when(productService.delete(anyInt())).thenReturn(Uni.createFrom().voidItem());
		Response response = productResource.deleteProduct(1).await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Product deleted successfully", apiResponse.getMessage());
	}

	@Test
	public void testCheckStock() {
		when(productService.isStockAvailable(anyInt(), anyInt())).thenReturn(Uni.createFrom().item(true));
		Response response = productResource.checkStock(1, 10).await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		assertEquals("Stock is available", apiResponse.getMessage());
	}

	@Test
	public void testGetProductsSortedByPrice() {
		Product product1 = new Product();
		product1.setName("Product 1");
		product1.setPrice(1000.0);
		Product product2 = new Product();
		product2.setName("Product 2");
		product2.setPrice(1000.0);
		when(productService.getSortedByPrice()).thenReturn(Uni.createFrom().item(List.of(product2, product1)));
		Response response = productResource.getProductsSortedByPrice().await().indefinitely();
		assertEquals(200, response.getStatus());
		ApiResponse<?> apiResponse = (ApiResponse<?>) response.getEntity();
		List<?> sortedList = (List<?>) apiResponse.getData();
		assertEquals("Product 2", ((Product) sortedList.get(0)).getName());
		assertEquals("Product 1", ((Product) sortedList.get(1)).getName());
	}
}
