package com.example.sertify;

import org.springframework.web.multipart.MultipartFile;

public interface adminRepo {

    void insertHashtoDB(MultipartFile file); 
}
