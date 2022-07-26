package com.winemarketv2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.winemarketv2.DTO.UserCustom;
import com.winemarketv2.Repository.UserRepository;
import com.winemarketv2.entity.User;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserCustom(user);
    }

    public UserDetails loadUserById(String id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new Exception();
        }
        return new UserCustom(user);
    }
    
}
