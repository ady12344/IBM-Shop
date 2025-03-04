package com.ibm.practica.shop.service;

import com.ibm.practica.shop.model.dto.EditProductDTO;
import com.ibm.practica.shop.model.entity.Product;
import com.ibm.practica.shop.model.dto.AddProductDTO;
import com.ibm.practica.shop.repository.ProductRepository;
import com.ibm.practica.shop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> get() {

            return productRepository.findAll();
    }



    public ResponseEntity<String> add(AddProductDTO product) {
        Product toBeSaved = Product.builder()
                .description(product.getDescription().toLowerCase())
                .name(product.getName().toLowerCase())
                .build();
        if(productRepository.findByName(toBeSaved.getName()).isPresent()){
            return ResponseEntity.badRequest().body("Product already exists!");
        }
        productRepository.save(toBeSaved);
        return ResponseEntity.ok("Product added!");
    }

    public ResponseEntity<String> deleteProductById(Long id){
        if(userRepository.findAll().stream().anyMatch(user -> user.getBasket().contains(productRepository.findById(id).get()))){
            return ResponseEntity.badRequest().body("Product is into the user basket");
        }
        if(productRepository.findById(id).isPresent()){
            productRepository.delete(productRepository.findById(id).get());
            return ResponseEntity.ok("Deleted succesfully!");
        }
        if(productRepository.count() == 0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("Product not found");
    }

    public boolean deleteProductByName(String name) throws NoSuchFieldException {
        log.info("Delete request for productID {}", name);
        Optional<Product> findByName = productRepository.findByName(name);

        if(userRepository.findAll().stream().anyMatch(user -> user.getBasket().contains(findByName.get()))){
            log.info("The product is into an user basket");
            return false;
        }

        if (productRepository.count() == 0){
            log.warn("ID {} not found in DB. Nothing to delete.",name);
            return false;
        }
        if(findByName.isPresent()){
            productRepository.delete(productRepository.findByName(name).get());
            return true;
        }

        return false;

    }

    public ResponseEntity<String> editProduct(Long id, EditProductDTO newProduct){
        log.info("edit request for productID {}", id);
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(byId.isPresent()){
            return ResponseEntity.badRequest().body("Another product with the same name already exists!");
        }

        byId.get().setName(newProduct.getName());
        byId.get().setDescription(newProduct.getDescription());
        productRepository.save(byId.get());
        return ResponseEntity.ok("Edited succesfully!");
    }
}
