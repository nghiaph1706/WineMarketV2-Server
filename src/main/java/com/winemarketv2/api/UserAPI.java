package com.winemarketv2.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.winemarketv2.DTO.LoginRequest;
import com.winemarketv2.DTO.UserCustom;
import com.winemarketv2.Repository.CartRepository;
import com.winemarketv2.Repository.UserRepository;
import com.winemarketv2.entity.Cart;
import com.winemarketv2.entity.User;
import com.winemarketv2.jwt.JwtTokenProvider;
import com.winemarketv2.service.EmailService;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/user/")
public class UserAPI {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired 
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired 
    EmailService emailService;

    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping(value="login")
    public Map<String,String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken((UserCustom) authentication.getPrincipal());
        String userId = userRepository.findByUsername(loginRequest.getUsername()).getId();
        Cart cart = cartRepository.findByUserIdAndStatusLike(userId, "None");
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setTotal(0);
            cart.setStatus("None");
            cartRepository.save(cart);
        }
        String cartId = cartRepository.findByUserIdAndStatusLike(userId, "None").getId();

        HashMap<String, String> response = new HashMap<String, String>();
        response.put("token", jwt);
        response.put("userId", userId);
        response.put("cartId", cartId);
        return response;
    }

    @PostMapping(value = "sendcode")
    public ResponseEntity<Object> sendCode(@Valid @RequestBody User user) throws UnsupportedEncodingException, MessagingException{
        String code ="";
		String un = user.getUsername();
		String em = user.getEmail();
        boolean check = false;

		List<User> users = userRepository.findAll();
		for (User u : users) {
			if (un.equals(u.getUsername()) && em.equals(u.getEmail())) {
				check = true;
				break;
			}
		}

		if (check) {
			Random random = new Random();
			for (int i = 1; i <= 6; i++) {
				code += random.nextInt(10);
			}
			Context context = new Context();
			context.setVariable("mainTitle", "Forgot Password Code");
			context.setVariable("secondTitle", code);
			emailService.sendMail(em, "Forgot Password Code.", "sub", context);
			return new ResponseEntity<Object>(Collections.singletonMap("code", code), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }

    @PostMapping(value = "forgot")
    public HttpStatus forgot(@Valid @RequestBody User user){
        User usern = userRepository.findByUsername(user.getUsername());
        if (usern == null) {
            return HttpStatus.NOT_FOUND;
        }
        usern.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(usern);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.CONFLICT;
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> uList = userRepository.findAll();
        if (uList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(uList,HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public ResponseEntity<User> getUser(@PathVariable("id") String id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public HttpStatus saveUser(@Valid @RequestBody User user ){
        try {
            userRepository.save(user);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.CONFLICT;
        }
    } 
    
    @PutMapping
    @PreAuthorize("(isAuthenticated() and #username == authentication.principal.username) or hasAuthority('ADMIN')")
    public HttpStatus updateUser(@Valid @RequestBody User user){
        User usern = userRepository.findById(user.getId()).orElse(null);
        if (usern == null) {
            return HttpStatus.NOT_FOUND;
        }
        usern = new User(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getImage(), user.isRole());
        return HttpStatus.OK;
    }

}
