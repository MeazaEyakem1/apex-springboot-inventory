package com.apex.eqp.inventory.services;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.helpers.ProductFilter;
import com.apex.eqp.inventory.repositories.InventoryRepository;
import com.apex.eqp.inventory.repositories.RecalledProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryRepository inventoryRepository;
    private final RecalledProductRepository recalledProductRepository;

    @Transactional
    public Product save(Product product) {
        return inventoryRepository.save(product);
    }

    public Collection<Product> getAllProduct() {
        // Retrieve recalled products from the repository
        List<RecalledProduct> recalledProducts = recalledProductRepository.findAll();

        // Extract product names from recalled products
        Set<String> productNames = new HashSet<>();
        for (RecalledProduct product : recalledProducts) {
            productNames.add(product.getName());
        }

        // Create a filter based on recalled product names
        ProductFilter filter = new ProductFilter(productNames);

        return filter.removeRecalledFrom(inventoryRepository.findAll());
    }
    public Optional<Product> findById(Integer id) {
        return inventoryRepository.findById(id);
    }
}
