package com.example.sertify;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class adminController {
    @Autowired
    private dataRepo repo;

    @GetMapping("/")
    public String homepage() {
        return "admin/homepage";
    }

    @GetMapping("/upload")
    public String showUpload(){
        return "admin/UploadFiles";
    }
    
    @GetMapping("/inputForm")
    public String showForm(){
        return "admin/inputForm";
    }


    @PostMapping("/upload/submit")
    public String submitFile(@RequestParam(value="file")MultipartFile file) {
        repo.insertHashtoDB(file);
        return "redirect:/admin/inputForm";
    }

    
    @PostMapping("/inputForm/submit")
    public String submitForm(@RequestParam(value="name")String name, @RequestParam(value="idNumber")String idNumber, @RequestParam(value="certificateNumber")String certificateNumber, @RequestParam(value="datePublished")String datePublished, @RequestParam(value="ownerPosition")String ownerPosition){
        return "redirect:/admin/watermarked";
    }
    
    @GetMapping("/watermarked")
    public String showWatermarked(){
        return "admin/watermarked";
    }
}