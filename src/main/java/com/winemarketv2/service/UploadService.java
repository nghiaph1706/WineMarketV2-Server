package com.winemarketv2.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class UploadService {
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "winemarket",
            "api_key", "265233159259853",
            "api_secret", "lidSRZrd8lf96tT53m8s1R0NPU4"));

    public void save(String folder,String fileName, MultipartFile multipartFile) throws IOException {
		if(multipartFile!=null){
			fileName = multipartFile.getOriginalFilename();
            File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+"imageTmp");
            multipartFile.transferTo(convFile);
            cloudinary.uploader().upload(convFile,
            ObjectUtils.asMap(
                "public_id", fileName.substring(0, fileName.lastIndexOf('.')),
                "folder", "winemarket/"+folder+"/"));
		}
    }
}
