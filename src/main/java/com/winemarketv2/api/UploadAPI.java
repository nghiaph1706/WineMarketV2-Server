package com.winemarketv2.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.winemarketv2.service.UploadService;

@RestController
@RequestMapping("/api/v1/upload/")
@PreAuthorize("isAuthenticated()")
public class UploadAPI {
    @Autowired
    UploadService uploadService;

    @PostMapping(value = "user")
    public HttpStatus userUpload(@RequestParam("file") MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            uploadService.save("user",fileName, multipartFile);
            return HttpStatus.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return HttpStatus.CONFLICT;
        }
    }

    @PostMapping(value = "product")
    public HttpStatus productUpload(@RequestParam("file") MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            uploadService.save("product",fileName, multipartFile);
            return HttpStatus.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return HttpStatus.CONFLICT;
        }
    }
}
