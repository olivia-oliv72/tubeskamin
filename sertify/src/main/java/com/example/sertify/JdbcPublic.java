package com.example.sertify;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class JdbcPublic implements publicRepo  {

    @Override
    public boolean verify(MultipartFile file) {
        if (true) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
