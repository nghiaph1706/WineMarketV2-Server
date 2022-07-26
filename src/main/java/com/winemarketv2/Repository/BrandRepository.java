package com.winemarketv2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.Brand;

public interface BrandRepository extends MongoRepository<Brand, String>{
    
}
