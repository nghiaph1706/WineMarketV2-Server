package com.winemarketv2.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public HttpStatus userUpload(@RequestParam("file") MultipartFile file){
        String path = "/mnt/SHARE DISK/WineMarketV2/WineMarketV2/src/assets/img/user/";
        try {
            uploadService.save(file, path);
            return HttpStatus.OK;
        } catch (Exception e) {
            System.out.println(e);
            return HttpStatus.CONFLICT;
        }
    }
}
