package com.winemarketv2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.CartDetails;

public interface CartDetailsRepository extends MongoRepository<CartDetails, String>{

    List<CartDetails> findByCartId(String id);

    CartDetails findByCartIdAndProductId(String cartId, String productId);

    List<CartDetails> findAllByCartId(String cartId);
    
}
