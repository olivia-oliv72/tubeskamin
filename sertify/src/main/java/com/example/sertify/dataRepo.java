package com.example.sertify;

import org.springframework.web.multipart.MultipartFile;

public interface dataRepo {

    void insertHashtoDB(MultipartFile file); 
}
