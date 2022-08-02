package com.winemarketv2.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.BrandRepository;
import com.winemarketv2.Repository.CategoryRepository;
import com.winemarketv2.Repository.ProductRepository;
import com.winemarketv2.entity.Product;

@RestController
@RequestMapping("/api/v1/product/")
public class ProductAPI {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

    @GetMapping
    public ResponseEntity<List<Object>> getProductLimit(
        @RequestParam("_limit") int limit,
        @RequestParam("_page") int page,
        @RequestParam("_sort") String sort,
        @RequestParam("_order") String order,
        @RequestParam("name_like") String namelike,
        @RequestParam("category_Id") String categorId,
        @RequestParam("brand_Id") String brandId
    ){
        List<Object> data = new ArrayList<>();
        Pageable pageable;
        if (order.equals("desc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        }
        List<Product> pList = productRepository.findByNameLikeAndCategoryIdLikeAndBrandIdLike(namelike,categorId,brandId,pageable);
        if (pList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Product prod : pList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", prod.getId());
            map.put("name", prod.getName());
            map.put("image", prod.getImage());
            map.put("category", categoryRepository.findById(prod.getCategoryId()).get().getName());
            map.put("brand", brandRepository.findById(prod.getBrandId()).get().getName());
            map.put("price", String.valueOf(prod.getPrice()));
            data.add(map);
        }
        return new ResponseEntity<List<Object>>(data, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Product> getCart(@PathVariable("id") String id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public HttpStatus saveProduct(@RequestBody Product product ){
        Product productn = new Product();
        productn.setName(product.getName());
        productn.setImage(product.getImage());
        productn.setPrice(product.getPrice());
        productn.setBrandId(product.getBrandId());
        productn.setCategoryId(product.getCategoryId());
        try {
            productRepository.save(productn);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.CONFLICT;
        }
    } 

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public HttpStatus updateProduct(@Valid @RequestBody Product product){
        Product productn = productRepository.findById(product.getId()).orElse(null);
        if (productn == null) {
            return HttpStatus.NOT_FOUND;
        }
        productn = new Product(productn.getId(), product.getName(), product.getImage(), product.getPrice(), product.getCategoryId(), product.getBrandId());
        productRepository.save(productn);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public HttpStatus deleteProduct(@PathVariable("id") String id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return HttpStatus.NOT_FOUND;
        }
        productRepository.delete(product);
        return HttpStatus.OK;
    }

}
