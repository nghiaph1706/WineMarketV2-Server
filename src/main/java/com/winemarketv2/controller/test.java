package com.winemarketv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winemarketv2.Repository.CategoryRepository;

@Controller
public class test {
    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping("/test")
    public String index(){
        System.out.println(categoryRepository.findAll().get(0).getName());
        return "";
    }
}
