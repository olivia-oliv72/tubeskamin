package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sertify")
public class sertifyController {
    
    @GetMapping("/upload")
    public String showUpload(){
        return "admin/UploadFile";
    }

    @PostMapping()
}