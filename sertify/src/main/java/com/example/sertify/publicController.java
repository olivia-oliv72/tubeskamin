package com.example.sertify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sertify")
public class publicController {
    
    @Autowired
    private publicRepo repo;

    @GetMapping("/")
    public String showHomepage() {
        return "public/homepage";
    }
    
    @GetMapping("/upload")
    public String showUploadPage() {
        return "public/UploadFiles";
    }

    @PostMapping("/upload/submit")
    public String submitUpload(@RequestParam(value = "file")MultipartFile file) {

        //CHECK FOR FILE SIZE FIRST
        //if wrong size
        //return"redirect:/sertify/sizeerror/";

        if (repo.verify(file)) {
            return"redirect:/sertify/success";
        }
        else {
            return"redirect:/sertify/failed";
        }
    }

    @GetMapping("/success")
    public String verifySuccess() {
        return"public/verifResultSuccess";
    }
    
    @GetMapping("/failed")
    public String verifyFailed() {
        return"public/verifResultFail";
    }
    
    @GetMapping("/sizeerror")
    public String showSizeError() {
        return"public/wrongSize";
    }
}
