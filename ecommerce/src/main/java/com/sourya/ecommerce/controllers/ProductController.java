package com.sourya.ecommerce.controllers;

import com.sourya.ecommerce.common.ApiResponse;
import com.sourya.ecommerce.dto.product.ProductDto;
import com.sourya.ecommerce.model.Category;
import com.sourya.ecommerce.service.CategoryService;
import com.sourya.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto){
        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
        if(!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }

        Category category = optionalCategory.get();
        productService.addProduct(productDto, category);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> productDtos = productService.listProducts();
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @PostMapping("/update/{productID}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Integer productID,
                                                     @RequestBody @Valid ProductDto productDto) {
        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category category = optionalCategory.get();
        productService.updateProduct(productID, productDto, category);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }

}
