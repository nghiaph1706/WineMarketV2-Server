package com.winemarketv2.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {
    
	@Autowired
	ServletContext app;
    
    public File save(MultipartFile file, String path) {
		if (!file.isEmpty()) {
			File dir = new File(app.getRealPath(path));
			System.out.println(app.getRealPath(path));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			try {
				File savedFile = new File(dir, file.getOriginalFilename());
				file.transferTo(savedFile);
				System.out.println("oke");
				return savedFile;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
