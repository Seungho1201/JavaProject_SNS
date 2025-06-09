package com.example.clonestagram.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    public String uploadToStaticImg(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // 저장 경로: static/img/
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/postImg";
        File dir = new File(uploadDir);

        if (!dir.exists()) dir.mkdirs();  // 폴더 없으면 생성

        // 파일명 중복 방지
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File target = new File(dir, fileName);

        // 저장
        file.transferTo(target);

        // 웹 브라우저에 보여줄 경로
        return "/postImg/" + fileName;
    }
}