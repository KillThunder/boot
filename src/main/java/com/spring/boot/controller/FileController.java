package com.spring.boot.controller;

import com.spring.boot.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileUtil fileUtil;

    /**
     * 上传文件到本地
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        return fileUtil.uploadFileToLocal(FileUtil.TEST_PATH, multipartFile);
    }

    /**
     * 下载本地文件
     *
     * @param fileName  文件名称
     * @param userAgent 判断是否是IE浏览器
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/download/{fn}")
    public Object downloadFile(@PathVariable("fn") String fileName, @RequestHeader(name = "User-Agent", required = false) String userAgent) throws Exception {
        File file = fileUtil.downloadOfLocal(FileUtil.TEST_PATH, fileName);
        //设置HTTP请求状态    200
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        //设置内容长度
        bodyBuilder.contentLength(file.length());
        //application/octet-stream：二进制流数据（最常见的文件下载）
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        //对文件名进行解码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        if (null != userAgent && userAgent.indexOf("MSIE") > 0) {
            bodyBuilder.header("Content-Disposition", "attachment; filename=" + fileName);
        } else {
            bodyBuilder.header("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
        }

        LOGGER.info("下载文件：" + fileName);
        return bodyBuilder.body(FileUtils.readFileToByteArray(file));
    }

    @GetMapping(value = "/test")
    public Object test() throws Exception {
        System.out.println("抛出异常");
//        Integer i = 100 / 0;
        if (true)
            throw new Exception("这个不行啊");

        return "success";
    }
}
