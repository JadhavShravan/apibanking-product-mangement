package com.apibanking.product;

import com.apibanking.product.dto.ApiResponse;
import com.apibanking.product.entity.Product;
import com.apibanking.product.service.ProductService;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@NonBlocking
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

	@Inject
	ProductService productService;

	@POST
	public Uni<Response> createProduct(Product product) {
		return productService.create(product).map(created -> Response.status(Response.Status.CREATED)
				.entity(new ApiResponse<>(201, "Product created successfully", created)).build());
	}

	@GET
	public Uni<Response> getAllProducts() {
		return productService.getAll().map(
				products -> Response.ok(new ApiResponse<>(200, "Product list fetched successfully", products)).build());
	}

	@GET
	@Path("/{id}")
	public Uni<Response> getProductById(@PathParam("id") Integer id) {
		return productService.getById(id)
				.map(productOpt -> productOpt
						.map(product -> Response.ok(new ApiResponse<>(200, "Product found", product)).build())
						.orElse(Response.status(Response.Status.NOT_FOUND)
								.entity(new ApiResponse<>(404, "Product not found", null)).build()));
	}

	@PUT
	@Path("/{id}")
	public Uni<Response> updateProduct(@PathParam("id") Integer id, Product updatedProduct) {
		return productService.update(id, updatedProduct)
				.map(product -> Response.ok(new ApiResponse<>(200, "Product updated successfully", product)).build());
	}

	@DELETE
	@Path("/{id}")
	public Uni<Response> deleteProduct(@PathParam("id") Integer id) {
		return productService.delete(id)
				.replaceWith(Response.ok(new ApiResponse<>(200, "Product deleted successfully", null)).build());
	}

	@GET
	@Path("/{id}/check-stock")
	public Uni<Response> checkStock(@PathParam("id") Integer id, @QueryParam("count") int count) {
		return productService.isStockAvailable(id, count).map(available -> {
			String message = available ? "Stock is available" : "Stock is not available";
			return Response.ok(new ApiResponse<>(200, message, available)).build();
		});
	}

	@GET
	@Path("/sorted-by-price")
	public Uni<Response> getProductsSortedByPrice() {
		return productService.getSortedByPrice()
				.map(sorted -> Response.ok(new ApiResponse<>(200, "Products sorted by price", sorted)).build());
	}
}
