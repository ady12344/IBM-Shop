package com.ibm.practica.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.practica.shop.model.dto.EditProductDTO;
import com.ibm.practica.shop.model.dto.ProductDTO;
import com.ibm.practica.shop.model.entity.Product;
import com.ibm.practica.shop.model.dto.AddProductDTO;
import com.ibm.practica.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    private final ObjectMapper mapper;

    @GetMapping("/getall")
    public List<ProductDTO> get() {

//        List<Product> fromService =productService.get();
//        List<ProductDTO> responseList = new ArrayList<>();
//        for (Product p : fromService){
//            ProductDTO dto = mapper.convertValue(p,ProductDTO.class);
//            responseList.add(dto);
//        }
//        return responseList;

        return productService.get()
                .stream()
                .map(product -> mapper.convertValue(product,ProductDTO.class))
                .toList();
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AddProductDTO newProduct) {
        log.info("adding newly received entity {}", newProduct);
        return productService.add(newProduct);
    }

    @DeleteMapping("/deleteProductByName")
    public ResponseEntity<String> delete(@RequestParam(name = "name") String name){

        try {
            return productService.deleteProductByName(name) ? ResponseEntity.accepted().build() :
                    ResponseEntity.notFound().build();
        } catch (NoSuchFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteProductById")
    public ResponseEntity<String> deleteProductById(@RequestParam("id") Long id){
        return productService.deleteProductById(id);
    }

    @PutMapping("/edit")
    public ResponseEntity<String> edit(@RequestParam(name = "id") Long id, @RequestBody EditProductDTO newProduct){
        log.info("edit request for productID {} with new details {}", id, newProduct);

        return productService.editProduct(id, newProduct);
    }
}
