package com.iteye.melin.web.model.support;


import org.springframework.web.multipart.commons.CommonsMultipartFile;
 
/**
 * 文件上传类
 */
public class FileUploadBean {
 
    private CommonsMultipartFile file;
 
    public CommonsMultipartFile getFile() {
        return file;
    }
 
    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }
}
