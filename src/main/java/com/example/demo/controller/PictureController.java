package com.example.demo.controller;

import com.amazonaws.Response;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PictureService;
import com.example.demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PictureController {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PictureService pictureService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadPicture(
            @PathVariable Long userId,
            @RequestPart(value = "images", required = false) MultipartFile image
    ) throws IOException {
        pictureService.save(image, userId);
        return ResponseEntity.ok("업로드 완료");
    }
}
