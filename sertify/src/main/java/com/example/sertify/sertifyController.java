package com.example.sertify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class sertifyController {
    
    @GetMapping("/")
    public String homepage() {
        return "admin/homepage";
    }

    @GetMapping("/upload")
    public String showUpload(){
        return "admin/UploadFiles";
    }

    @GetMapping("/inputForm")
    public String inputForm() {
        return "admin/inputForm";
    }
}