package com.winemarketv2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.Cart;

public interface CartRepository  extends MongoRepository<Cart,String>{

    Cart findByUserIdAndStatusLike(String userId, String string);
    
}
