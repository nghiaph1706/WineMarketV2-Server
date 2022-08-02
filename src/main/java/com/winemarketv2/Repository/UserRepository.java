package com.winemarketv2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.winemarketv2.entity.User;

public interface UserRepository extends MongoRepository<User,String>{

    User findByUsername(String username);

    List<User> findByUsernameLikeAndEmailLikeAndRoleTrue(String username, String email);

    List<User> findByUsernameLikeAndEmailLikeAndRoleFalse(String username, String email);
    
}
