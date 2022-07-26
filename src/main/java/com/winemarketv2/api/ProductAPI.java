package com.winemarketv2.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.ProductRepository;
import com.winemarketv2.entity.Product;

import lombok.var;

@RestController
@RequestMapping("/api/v1/product/")
public class ProductAPI {
    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getProductLimit(
        @RequestParam("_limit") int limit,
        @RequestParam("_page") int page,
        @RequestParam("_sort") String sort,
        @RequestParam("_order") String order,
        @RequestParam("name_like") String namelike,
        @RequestParam("category_Id") String categorId,
        @RequestParam("brand_Id") String brandId
    ){
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
        return new ResponseEntity<List<Product>>(pList, HttpStatus.OK);
    }
}
