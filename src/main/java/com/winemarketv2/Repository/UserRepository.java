package com.winemarketv2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.User;

public interface UserRepository extends MongoRepository<User,String>{

    User findByUsername(String username);
    
}
