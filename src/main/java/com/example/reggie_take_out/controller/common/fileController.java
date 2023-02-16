package com.example.reggie_take_out.controller.common;

import com.example.reggie_take_out.respR.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@ResponseBody
@RequestMapping("/common")
@CrossOrigin(origins = "*",maxAge = 3600)
@Slf4j
public class fileController {

    @Value("${reggie.path}")
    private String basePath;

    /*
    *   文件上传
    * */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        // 此时的file是临时文件，需要进行转存
        log.info(file.toString());
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用UUID重新生成文件名，防止文件名称重复
        StringBuffer fileName = new StringBuffer(UUID.randomUUID().toString());
        fileName.append(suffix);
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            // 将临时文件存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(fileName.toString());
    }

    /*
     *   文件加载
     * */
    @GetMapping("/download")
    public void download(@PathParam("name") String name, HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream,outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
