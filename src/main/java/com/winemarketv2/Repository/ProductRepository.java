package com.winemarketv2.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.Product;

public interface ProductRepository extends MongoRepository<Product,String> {

    List<Product> findByNameLikeAndCategoryIdLikeAndBrandIdLike(String namelike, String categorId, String brandId,
            Pageable pageable);
    
}
