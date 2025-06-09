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

        // ✅ 외부 경로에 저장 (프로젝트 루트/uploads/postImg)
        String uploadDir = System.getProperty("user.dir") + "/uploads/postImg";
        File dir = new File(uploadDir);

        if (!dir.exists()) dir.mkdirs();  // 폴더 없으면 생성

        // 파일명 중복 방지
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File target = new File(dir, fileName);

        // 저장
        file.transferTo(target);

        // ✅ 브라우저에 보여줄 경로는 "/uploads/postImg/파일명"
        return "/uploads/postImg/" + fileName;
    }
}
