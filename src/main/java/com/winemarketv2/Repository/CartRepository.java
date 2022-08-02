package com.winemarketv2.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.Cart;

public interface CartRepository  extends MongoRepository<Cart,String>{

    Cart findByUserIdAndStatusLike(String userId, String string);

    void deleteAllByUserId(String id);

    List<Cart> findByUserIdLikeAndStatusLike(Pageable pageable, String userId, String status);
    
}
