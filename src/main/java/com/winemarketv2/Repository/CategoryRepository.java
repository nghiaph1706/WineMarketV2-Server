package com.winemarketv2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.Category;

public interface CategoryRepository extends MongoRepository<Category, String>{
    
}
