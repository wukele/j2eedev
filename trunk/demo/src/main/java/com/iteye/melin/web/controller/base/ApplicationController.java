package com.iteye.melin.web.controller.base;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.DictionaryHolder;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Application;
import com.iteye.melin.web.model.support.AppFile;
import com.iteye.melin.web.model.support.AppSnap;
import com.iteye.melin.web.model.support.AppType;
import com.iteye.melin.web.service.base.ApplicationService;
import com.iteye.melin.web.service.support.AppFileService;
import com.iteye.melin.web.service.support.AppSnapService;
import com.iteye.melin.web.service.support.AppTypeService;

/**
 * 应用管理Controller
 * 
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/application")
public class ApplicationController extends BaseController {
	// ~ Instance fields
	// ================================================================================================
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private AppTypeService appTypeService;
	@Autowired
	private AppFileService appFileService;
	@Autowired
	private AppSnapService appSnapService;
	// ~ Methods
	// ========================================================================================================
	@RequestMapping("/index")
	public String index() {
		return "admin/base/application";
	}

	@RequestMapping("/pageQueryApplications")
	@ResponseBody
	public Page<Application> pageQueryApplications(
			@RequestParam("start") int startIndex,
			@RequestParam("limit") int pageSize, Application application,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String dir) {
		PageRequest<Application> pageRequest = new PageRequest<Application>(
				startIndex, pageSize);

		if (StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);

		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		Map<String, Object> fliters = pageRequest.getFilters();
		/* 按分类 【全部分类除外】*/
		if (application.getTypeId() != null && application.getTypeId() != 1) {
			fliters.put("typeId", application.getTypeId());
		}
		/* 按名称 */
		if (StringUtils.hasText(application.getAppName()))
			likeFilters.put("appName", application.getAppName());

		/* 按关键字 【后台以逗号分隔写入关键字】*/
		if (StringUtils.hasText(application.getKeyWords()))
			likeFilters.put("keyWords", application.getKeyWords());

		Page<Application> page = applicationService.findAllForPage(pageRequest);
		DictionaryHolder.transfercoder(page.getResult(), 10009L, "getAppState");
		List<AppType> typeList = appTypeService.findAllEntity();
		List<AppFile> fileList = appFileService.findAllEntity();
		for (Application o : page.getResult()) {
			/* appType_Name 应用分类 */
			for (AppType t : typeList) {
				if (t.getId().equals(o.getTypeId())) {
					o.setTypeId_Name(t.getTypeName());
					break;
				}
			}
			/* download_Site 下载地址 */
			for (AppFile f : fileList) {
				if (f.getId().equals(o.getFileId())) {
					o.setDlClient(f.getFilepath());
				}
			}
		}
		return page;
	}

	/************************************************
	 * ①上传文件和图片
	 * ②图片保存到/resources/apk/images/apk/
	 * ③文件保存到 /resources/apk/upload/
	 * ④读取apk文件信息
	 * ⑤抽取文件基本信息分别入库
	 * ***********************************************
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertApplication", method = RequestMethod.POST)
	@ResponseBody
	public String insertApplication(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
	    File savefile_apk = null  , savefile_icon =null , savefile_snap =null;
		String appName /*名称*/ , appState /*状态*/, keyWords /*关键字*/, typeId /*分类*/,appPackage /*包名*/ = "",verMark = null,/*版本*/
		filename_apk /*apk路径 */,fileext_apk /*扩展名*/,filename_icon /*icon路径 */,fileext_icon ,/*扩展名*/filename_snap/*apk截图*/,fileext_snap/*扩展名*/
		,app_sdk_ver /*sdk版本*/,app_author /*作者*/,app_summary /*摘要*/,app_desc/*描述*/ ;
		long filesize_app ;/*app大小*/;
		/*定义文件数组*/
		List<File> files = new ArrayList<File>();
		files.add(savefile_apk);
		files.add(savefile_icon);
		files.add(savefile_snap);
		try {
			List<FileItem> items = upload.parseRequest(request);
			Map<String, Serializable> fields = new HashMap<String, Serializable>();
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField())
					fields.put(item.getFieldName(), item.getString());
				else
					fields.put(item.getFieldName(), item);
			}
			/* 文件存储在容器中的绝对路径 */
			String folder = request.getSession().getServletContext()
					.getRealPath("/");
			String dir_apk = folder + File.separator + "resources\\apk\\upload"
					+ File.separator;
			String dir_img = folder + File.separator + "resources\\apk\\images"
					+ File.separator;
			String dir_snap = folder + File.separator + "resources\\apk\\snap"
					+ File.separator;
			File f1 = new File(dir_apk);
			File f2 = new File(dir_img);
			File f3 = new File(dir_snap);
			if (!f1.exists()) {
				f1.mkdirs();
			}
			if (!f2.exists()) {
				f2.mkdirs();
			}
			if (!f3.exists()) {
				f3.mkdirs();
			}
			/* 抽取表单基本属性 */
			appName = (String)fields.get("appName");
			appState = (String)fields.get("appState");
			keyWords = (String)fields.get("keyWords");
			typeId	= (String)fields.get("typeId");
			/*非必填扩展信息*/
			app_sdk_ver =(String)fields.get("minSdkVer");
			app_author=(String)fields.get("authorName");
			app_summary = (String)fields.get("appSummary");
			app_desc = (String)fields.get("appDesc");
			/* 获取表单的上传文件 */
			FileItem uploadFile_apk = (FileItem) fields.get("id_app_apk");
			FileItem uploadFile_icon = (FileItem) fields.get("id_app_icon");
			FileItem uploadFile_snap = (FileItem) fields.get("id_app_snap1");
			/* 获取文件上传路径名称 */
			String fileName_apk = uploadFile_apk.getName();
			String fileName_icon = uploadFile_icon.getName();
			String fileName_snap = uploadFile_snap.getName();
			/* 获取文件扩展名 */
		    fileext_apk = fileName_apk.substring(fileName_apk
					.lastIndexOf(".") + 1);
			fileext_icon = fileName_icon.substring(fileName_icon
					.lastIndexOf(".") + 1);
			fileext_snap = fileName_snap.substring(fileName_snap
					.lastIndexOf(".") + 1);
			/* 文件校检 */
			String checkresult_apk = applicationService.fileVertify(
					fileext_apk, uploadFile_apk);
			String checkresult_icon = applicationService.fileVertify(
					fileext_icon, uploadFile_icon);
//			String checkresult_snap = applicationService.fileVertify(
//					fileext_snap, uploadFile_snap);
			/* 返回异常信息 */
			if (!checkresult_apk.equals("pass") || !checkresult_icon.equals("pass")) {
//				return checkresult_apk.equals("pass") ? "{success:false ,err:"
//						+ checkresult_icon + "}" :(checkresult_icon.equals("pass") ?"{success:false, err:"+checkresult_snap: "{success:false ,err:"
//							+ checkresult_apk + "}");
				return "{success:false , err: '异常'}";
			}
			/* 重命名文件 */
			filename_apk = UUID.randomUUID().toString();
			filename_icon = UUID.randomUUID().toString();
			filename_snap = UUID.randomUUID().toString();
			savefile_apk = new File(dir_apk + filename_apk + "."
					+ fileext_apk);
			savefile_icon = new File(dir_img + filename_icon + "."
					+ fileext_icon);
			savefile_snap = new  File(dir_img + filename_snap + "."
					+ fileext_snap);
			/* 获取文件大 小*/
			filesize_app = uploadFile_apk.getSize();
			/* 存储上传文件 */
			uploadFile_apk.write(savefile_apk);
			uploadFile_icon.write(savefile_icon);
			uploadFile_snap.write(savefile_snap);

		} catch (Exception ex) {
			/* 清理垃圾文件 */
			applicationService.clear(files);
			logger.error("=err==" + ex.getMessage());
			return "{success:false ,err:文件信息保存失败";
		}
		//==========================以下是入库操作===========================================
		/* 保存成功后读取apk包信息 */
		List<String> apkInfos = applicationService.readApkInfo(savefile_apk);
		if(!apkInfos.isEmpty()){
			appPackage = apkInfos.get(1);
			verMark = apkInfos.get(2);
		}
		/* 新建应用文件 */
		AppFile newAppfile = new AppFile();
		newAppfile.setFileName(filename_apk+"."+fileext_apk);
		newAppfile.setFilepath("/resources/apk/upload/"+filename_apk+"."+fileext_apk);
		newAppfile.setFilesize(new Integer((int)filesize_app));
		appFileService.insertEntity(newAppfile);
		Long fileId = newAppfile.getId();
		if(fileId == null){
			logger.error("apk文件信息保存失败");
			/* 清理垃圾文件 */
			applicationService.clear(files);
			return  "{success:false ,err:文件信息保存失败";
		}
		/*新建应用包*/
		Application newApp = new Application();
		//*sdk版本*/,app_author /*作者*/,app_summary /*摘要*/,app_desc/*描述*/ 
		newApp.setMinSdkVer(app_sdk_ver);
		newApp.setAuthorName(app_author);
		newApp.setAppSummary(app_summary);
		newApp.setAppDesc(app_desc);
		newApp.setFileSize(new Float(filesize_app));
		newApp.setAppName(appName);
		newApp.setAppState(Short.valueOf(appState));
		newApp.setTypeId(Integer.valueOf(typeId));
		newApp.setKeyWords(keyWords);
		newApp.setDownTimes(0);
		newApp.setIconUrl("/resources/apk/images/"+filename_icon+"."+fileext_icon);
		newApp.setFileId(fileId);
		newApp.setAppPackage(appPackage);
		newApp.setGlobalMark(UUID.randomUUID().getLeastSignificantBits());  //yy全局标识
		newApp.setCommentTimes(0);
		newApp.setSoftLevel((short)0);
		Date d = new Date();
		newApp.setCreateTime(d);
		newApp.setUpdateTime(d);
		if(verMark!=null){
			newApp.setVerMark(Integer.parseInt(verMark));	
		}
		applicationService.insertEntity(newApp);
		Long appId = newApp.getId();
		if(appId == null){
			logger.error("apk应用信息保存失败");
			/* 清理垃圾文件 */
			applicationService.clear(files);
			/* 撤销文件记录*/
			appFileService.deleteEntity(fileId);
			return  "{success:false ,err:apk应用信息保存失败";
		}
		//新建截图
		AppSnap newSnap = new AppSnap();
		newSnap.setAppId(appId);
		newSnap.setFileId(fileId);
		newSnap.setSnapUrl("/resources/apk/snap/"+filename_snap+"."+fileext_snap);
		appSnapService.insertEntity(newSnap);
		Long snapId = newSnap.getId();
		if(snapId == null){
			logger.error("截图保存失败");
			/* 清理垃圾文件 */
			applicationService.clear(files);
			/* 撤销文件记录*/
			appFileService.deleteEntity(fileId);
			return  "{success:false ,err:'截图保存失败'";
		}
		//return ResponseData.SUCCESS_NO_DATA.toString();
		return "{success:true}";
	}

	@RequestMapping(value = "/loadApplication", method = RequestMethod.POST)
	@ResponseBody
	public Application loadApplication(Long id) {
		return applicationService.findEntity(id);
	}

	@RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData updateApplication(Application application) {
		applicationService.updateEntity(application);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value = "/deleteApplication", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		applicationService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value = "/updateAppDownTimes", method = RequestMethod.POST)
	@ResponseBody
	public String updateAppDownTimes(Long appId) {
		Application app = applicationService.findEntity(appId);
		app.setDownTimes(app.getDownTimes().intValue() + 1);
		applicationService.createOrUpdate(app);
		return ResponseData.SUCCESS_NO_DATA.toString();
	}
}
