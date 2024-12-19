package com.example.demo.service;

import com.example.demo.entity.Picture;
import com.example.demo.entity.Users;
import com.example.demo.repository.PictureRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void save(MultipartFile image, Long userId ) throws IOException {
        Users findUser = userRepository.findByIdOrElseThrow(userId);

        //picture 테이블에 담을 변수 생성
        String category = findUser.getClass().getSimpleName();
        //업로드 된 이미지 파일 주소
        String publicUrl = s3Service.uploadImage(image);

        // picture 저장
        Picture picture = new Picture(publicUrl, category, findUser);

        pictureRepository.save(picture);
    }
}
