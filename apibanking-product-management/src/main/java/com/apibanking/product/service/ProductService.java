package com.apibanking.product.service;

import com.apibanking.product.entity.Product;
import com.apibanking.product.repository.ProductRepository;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {

	@Inject
	ProductRepository repository;

	@WithTransaction
	public Uni<Product> create(Product product) {
		return repository.persist(product);
	}

	@WithSession
	public Uni<List<Product>> getAll() {
		return repository.listAll();
	}

	@WithSession
	public Uni<Optional<Product>> getById(Integer id) {
		return repository.findById(id).map(Optional::ofNullable);
	}

	@WithTransaction
	public Uni<Product> update(Integer id, Product updatedProduct) {
		return repository.findById(id).onItem().ifNotNull().transformToUni(existing -> {
			existing.setName(updatedProduct.getName());
			existing.setDescription(updatedProduct.getDescription());
			existing.setPrice(updatedProduct.getPrice());
			existing.setQuantity(updatedProduct.getQuantity());
			return repository.persist(existing);
		});
	}

	@WithTransaction
	public Uni<Void> delete(Integer id) {
		return repository.findById(id).onItem().ifNotNull().transformToUni(product -> repository.delete(product))
				.replaceWithVoid();
	}

	@WithSession
	public Uni<Boolean> isStockAvailable(Integer id, int count) {
		return repository.findById(id).map(product -> product != null && product.getQuantity() >= count);
	}

	@WithSession
	public Uni<List<Product>> getSortedByPrice() {
		return repository.list("ORDER BY price ASC");
	}
}
