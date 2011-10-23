package com.iteye.melin.web.service.base.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.util.GetInfo;
import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.ApplicationDao;
import com.iteye.melin.web.model.base.Application;
import com.iteye.melin.web.service.base.ApplicationService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<Application, Long> implements ApplicationService {
	//~ Instance fields ================================================================================================
	@Autowired
	private ApplicationDao applicationDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Application, Long> getGenericDao() {
		return this.applicationDao;
	}

	@Override
	public String fileVertify(String fileExt , FileItem uploadFile) {
		long maxSize = 1024*1024*10;
		/* 检查文件类型 */
		if (("," + fileExt.toLowerCase() + ",").indexOf(","+ "exe".toLowerCase() + ",") > 0) {
			return "不允许上传此类型的文件";
		}
		/* 文件是否为空 */
		if (uploadFile.getSize() == 0) {
			return "上传文件不能为空";
		}
		/* 检查文件大小 */
		if(uploadFile.getSize() > maxSize) {
			return  "上传文件的大小超出限制,最大不能超过" + 1024*1024*10 + "M";
		}
		return "pass";
	}
	
	@Override
	 public  void clear(List<File> files){
		 if(!files.isEmpty()){
			 Iterator<File> fi = files.iterator();
			 while(fi.hasNext()){
				 File temp = fi.next();
				 if(temp!=null&&temp.exists()){
					 temp.delete();
				 }
			 }
		 }
	 }

	@Override
	public List<String> readApkInfo(File apk) {
		return  GetInfo.getApkInfo(apk.getAbsolutePath());
	}	
	
}
