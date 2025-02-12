package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RestController
@Api(tags = "文件通用接口")
@RequestMapping("/admin/common")
public class CommonController {
//TODO ：文件上传问题
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public void upload(MultipartFile file, HttpServletResponse response) throws Exception {
        File dir = new File("C:\\cangqiong转存图片");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String realPath = dir.getCanonicalPath();
        file.transferTo(new File(realPath + "/" + "hello.jpg"));
        try (
             FileInputStream fis = new FileInputStream(new File(realPath + "/" + "hello.jpg"));
             ServletOutputStream sos = response.getOutputStream()
        ) {
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                sos.write(bytes, 0, len);
                sos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
