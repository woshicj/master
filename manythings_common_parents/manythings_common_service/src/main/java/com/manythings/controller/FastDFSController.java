package com.manythings.controller;

import com.manythings.util.AjaxResult;
import com.manythings.util.FastDfsApiOpr;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/common")
public class FastDFSController {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public AjaxResult upload(@RequestBody MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        try {
            String filepath = FastDfsApiOpr.upload(file.getBytes(), extension);
            return AjaxResult.me().setSuccess(true).setMsg("上传成功！").setObject(filepath);

        } catch (IOException e) {
            return AjaxResult.me().setObject(false).setMsg(e.getMessage());
        }
    }

}
