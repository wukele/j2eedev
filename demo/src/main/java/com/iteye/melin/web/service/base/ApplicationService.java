package com.iteye.melin.web.service.base;

import java.io.File;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import com.iteye.melin.core.web.service.BaseService;
import com.iteye.melin.web.model.base.Application;

/**
 *
 * @datetime 2010-8-9 上午10:34:58
 * @author libinsong1204@gmail.com
 */
public interface ApplicationService extends BaseService<Application, Long> {
	
	/**
	 * 验证文件有效性
	 * @param fileExt  文件扩展名
	 * @param uploadFile  MIME类型文件
	 * @return
	 */
    public  String  fileVertify(String fileExt , FileItem uploadFile);
    
    /**
     * 清理垃圾文件
     * @param files 文件数组
     */
    public  void clear(List<File> files);
    
    /**
     * 读取apk文件信息
     * @param  apk
     * @return index  1-path 2-package 3-version
     */
    public  List<String> readApkInfo(File apk);
    
    /**
     * 判断推荐状态
     * @param appId
     * @return true 已经被推荐
     *         false 尚未被推荐
     */
    public boolean alrdybeReco(Long appId);
}
