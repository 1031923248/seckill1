package com.amane.seckill.controller;

import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
@Slf4j
public class FileUploadController {
    @RequestMapping("/upload")
    @ResponseBody
    public RespBean upload(@RequestPart("file")MultipartFile goodImg) throws IOException {
        String originalFilename = null;
        if(!goodImg.isEmpty()){
            if (goodImg.getSize() >= 4096 * 1024){
                return RespBean.error(RespBeanEnum.SIZE_OVER);
            }
            //保存到文件服务器，OSS服务器
             originalFilename = goodImg.getOriginalFilename();
            log.info(originalFilename);
            goodImg.transferTo(new File("D:\\"+originalFilename));
        }
        return RespBean.success(originalFilename);
    }
}
