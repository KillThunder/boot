package com.spring.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Component
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    @Value("${file.testPath}")
    private String testPath;

    public static String TEST_PATH = "testPath";

    /**
     * 获取文件路径
     *
     * @param filePath
     * @return
     */
    private String getFilePath(String filePath) {
        switch (filePath) {
            case "testPath":
                filePath = testPath;
                break;
            default:
                throw new RuntimeException();
        }
        return filePath;
    }

    /**
     * 上传文件到本地
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public String uploadFileToLocal(String path, MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();

        LOGGER.info("上传文件名称：" + fileName);
        LOGGER.info("上传文件MIME类型：" + multipartFile.getContentType());
        LOGGER.info("表单中文件组件的名字：" + multipartFile.getName());
        LOGGER.info("上传文件字节大小：" + multipartFile.getSize());

        File filePath = new File(getFilePath(path), fileName);
        if (!filePath.getParentFile().exists()) {
            LOGGER.info("文件夹不存在，创建文件夹：" + filePath.getParentFile());
            filePath.getParentFile().mkdir();
        }
        LOGGER.info("开始保存文件...");
        multipartFile.transferTo(new File(getFilePath(path) + File.separator + fileName));
        LOGGER.info("文件上传成功！");

        return fileName;
    }

    /**
     * 下载本地文件
     *
     * @param fileName 文件名
     * @return
     * @throws Exception
     */
    public File downloadOfLocal(String path, String fileName) throws Exception {
        File file = new File(getFilePath(path) + File.separator + fileName);

        return file;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param fileName 文件名
     */
    public void downloadFile(HttpServletResponse response, String fileName) {
        fileName = fileName;
        File file = new File(fileName);
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            LOGGER.info("获取文件成功：" + fileName);
        } catch (Exception e) {
            LOGGER.error("读取文件失败：" + fileName, e);
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                LOGGER.error("关闭文件流失败！" + fileName + e);
                e.printStackTrace();
            }

        }
    }
}
