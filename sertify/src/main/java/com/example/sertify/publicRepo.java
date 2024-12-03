package com.example.sertify;

import org.springframework.web.multipart.MultipartFile;

public interface publicRepo {
    boolean verify(MultipartFile file);
}
