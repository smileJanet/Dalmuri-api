package com.dalmuri.dmr.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
@CrossOrigin(origins="http://localhost:1003") // 이거 없으면 통신 안됨
public class FileUploadController { 

    @PostMapping("/uploads")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "No file uploaded"));
        }

        try{
            // (1) 업로드 경로 설정
            String uploadDir = "api/uploads/";
            File dir = new File(uploadDir);
            if(!dir.exists()) dir.mkdirs();
            // System.out.println("업로드 절대경로: " + new File(uploadDir).getAbsolutePath()); // 업로드 절대경로: C:\\Dalmuri-api\\api\\uploads

            // (2) 파일명 지정
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // (3) 저장된 파일 url 반환
            String fileUrl = "http://localhost:3001/uploads/" + fileName;
            return ResponseEntity.ok(Map.of("fileUrl", fileUrl));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to save files"));
        }

    }
}
