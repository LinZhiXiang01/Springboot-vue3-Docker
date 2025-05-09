package com.mocha.springboot.controller;


import cn.hutool.core.io.FileUtil;
import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final String storePath = System.getProperty("user.dir")+"/files/";
    @PostMapping("/upload")
    public ResultCode upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if(!FileUtil.isDirectory(storePath)){
            FileUtil.mkdir(storePath);
        }
        String fileName = System.currentTimeMillis() + "_" +originalFilename;
        String realPath = storePath + fileName;

        try{
            FileUtil.writeBytes(file.getBytes(),realPath);
        }catch(IOException e){
            e.printStackTrace();
            throw new CustomException("上传失败", 500);
        }

        String url = "http://121.37.41.250:8080/files/download/" + fileName;
        return ResultCode.success(url);
    }


    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response){
        try{
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(fileName), StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");

            OutputStream os = response.getOutputStream();
            String realPath = storePath + fileName;

            //获取到文件的字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes); //将字节数组写入到输出流中
            os.flush();//flush输出流
            os.close();
        }catch(IOException e){
            e.printStackTrace();
            throw new CustomException("下载失败", 500);
        }

    }

    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if(!FileUtil.isDirectory(storePath)){
            FileUtil.mkdir(storePath);
        }
        String fileName = System.currentTimeMillis() + "_" +originalFilename;
        String realPath = storePath + fileName;

        try{
            FileUtil.writeBytes(file.getBytes(),realPath);
        }catch(IOException e){
            e.printStackTrace();
            throw new CustomException("上传失败", 500);
        }

        String url = "http://121.37.41.250:8080/files/download/" + fileName;
        // wangEditor上传图片成功后， 需要返回的参数
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
        resMap.put("errno", 0);
        resMap.put("data", list);
        return resMap;
    }
}