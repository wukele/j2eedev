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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.DictionaryHolder;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Application;
import com.iteye.melin.web.model.base.Recommend;
import com.iteye.melin.web.model.support.AppFile;
import com.iteye.melin.web.model.support.AppRecoRs;
import com.iteye.melin.web.model.support.AppSnap;
import com.iteye.melin.web.model.support.AppType;
import com.iteye.melin.web.service.base.ApplicationService;
import com.iteye.melin.web.service.base.RecommendService;
import com.iteye.melin.web.service.support.AppFileService;
import com.iteye.melin.web.service.support.AppRecoRsService;
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
public class ApplicationController extends BaseController implements ServletContextAware{
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
	@Autowired
	private RecommendService recommendService;
	@Autowired
	private AppRecoRsService appRecoRsService;
	
	private ServletContext servletContext;  //获取ctx路径
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
		if (application.getTypeId() != null && application.getTypeId() != 1L) {
			fliters.put("typeId", application.getTypeId());
		}
		/* 按名称 */
		if (StringUtils.hasText(application.getAppName()))
			likeFilters.put("appName", application.getAppName());

		/* 按关键字 【后台以逗号分隔写入关键字】*/
		if (StringUtils.hasText(application.getKeyWords()))
			likeFilters.put("keyWords", application.getKeyWords());
		
		/* 按专题查询：  -1和null表无专题 ，-2表全部专题，其他表指定专题*/
		List<AppRecoRs> specialList = null;
		if(application.getSpecial()!=null && application.getSpecial()!= -1){
			if(application.getSpecial()!= -2){
				//查询指定专题
				logger.info("当前查询的专题是---"+application.getSpecial());
				specialList = appRecoRsService.findByProperty("recoId", application.getSpecial().longValue()); //指定专题
			}else{
				//查询全部专题
				specialList  = new ArrayList<AppRecoRs>();
				List<Recommend> specials = recommendService.findByProperty("type", 4); //所有专题
				for(Recommend r : specials){
					specialList.addAll(appRecoRsService.findByProperty("recoId", r.getId()));					
				}
			}
		}
		Page<Application> page = applicationService.findAllForPage(pageRequest);
		DictionaryHolder.transfercoder(page.getResult(), 10009L, "getAppState");
		List<AppType> typeList = appTypeService.findAllEntity();
		List<AppFile> fileList = appFileService.findAllEntity();
		List<Application> applist = page.getResult();
		for (int i=0;i<applist.size();i++) {
			/* special 专题*/
			Long special = null;
			boolean isExist = false;  //false---不符合专题的app ， true---符合专题的app
			if(specialList!=null && !specialList.isEmpty()){
				//移除非专题的application
				for(AppRecoRs s : specialList){
					if(s.getGlobalMark()== applist.get(i).getGlobalMark()){
						isExist = true;
						special = s.getRecoId();
						break;
					}
				}
			}
			if(application.getSpecial()!=null && application.getSpecial()!= -1 && !isExist ) {//-1表无专题查询
				//不存在或不符合该专题的都将从列表移去【注：做无专题查询除外】
				applist.remove(i);
				i--;
				continue;
			};
			/* appType_Name 应用分类 */
			for (AppType t : typeList) {
				if (t.getId().equals(applist.get(i).getTypeId())) {
					applist.get(i).setTypeId_Name(t.getTypeName());
					break;
				}
			}
			/* download_Site 下载地址 */
			for (AppFile f : fileList) {
				if (f.getId().equals(applist.get(i).getFileId())) {
					applist.get(i).setDlClient(f.getFilepath());
					break;
				}
			}			
			/* app专题 名称 */
			if(special!=null){			
				String special_Name = recommendService.findEntity(special).getName();
				applist.get(i).setSpecial_Name(special_Name);
			}			
			/*设置推荐状态*/
			if(applicationService.alrdybeReco(applist.get(i).getId()))
				applist.get(i).setHasAlrdyReco(true);
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
	    File savefile_apk = null  , savefile_icon =null;
		String appName /*名称*/ , appState /*状态*/, keyWords /*关键字*/, typeId /*分类*/,appPackage /*包名*/ = "",appVer= null,/*版本*/
		filename_apk /*apk路径 */,fileext_apk /*扩展名*/,filename_icon /*icon路径 */,fileext_icon ,/*扩展名*/
		app_sdk_ver /*sdk版本*/,app_author /*作者*/,app_summary /*摘要*/,app_desc,/*描述*/appWebsite/*软件网址*/,supportEmail/*技术支持email*/;
		int snapIndex =0;/*默认截图*/
		String[] filename_snap = new String[5];	//截图保存后的名称
		String[] fileext_snap  = new String[5]; //获取截图扩展名
		File[] savefile_snap = new File[5];  	//保存后文件的路径名
		long filesize_app ; //文件大小
		/*定义文件数组*/
		List<File> files = new ArrayList<File>();
		files.add(savefile_apk);
		files.add(savefile_icon);
		for(int i=0;i<savefile_snap.length;i++){
			files.add(savefile_snap[i]);	
		}
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
			/*抽取表单必填信息 */
			appName = (String)fields.get("appName");
			appState = (String)fields.get("appState");
			keyWords = (String)fields.get("keyWords");
			typeId	= (String)fields.get("typeId");
			snapIndex = Integer.parseInt((String)fields.get("snapIndex")); //获取默认截图索引
			/*非必填信息*/
			app_sdk_ver =(String)fields.get("minSdkVer");
			app_author=(String)fields.get("authorName");
			app_summary = (String)fields.get("appSummary");
			app_desc = (String)fields.get("appDesc");
			appWebsite = (String)fields.get("appWebsite");
			supportEmail = (String)fields.get("supportEmail");
			/* 获取表单的上传文件 */
			FileItem uploadFile_apk = (FileItem) fields.get("id_app_apk");
			FileItem uploadFile_icon = (FileItem) fields.get("id_app_icon");
			/*********保存截图******/
			FileItem[] uploadFile_snap =new FileItem[5]; 	//获取截图文件
			String[] fileName_snap = new String[5]; 		//获取截图路径名
			for(int i=0;i<5 ;i++){
				if(fields.get("id_app_snap"+i)==null){  
					continue;
				}
				uploadFile_snap[i] = (FileItem)fields.get("id_app_snap"+i);
				fileName_snap[i] = uploadFile_snap[i].getName();
				fileext_snap[i] = fileName_snap[i].substring(fileName_snap[i].lastIndexOf(".")+1);		//验证		
				filename_snap[i] = UUID.randomUUID().toString();
				if(snapIndex==i){
					savefile_snap[i]=new File(dir_snap + filename_snap[i] + "__default."  //默认图片标识
							+ fileext_snap[i]);
					uploadFile_snap[i].write(savefile_snap[i]);
					uploadFile_snap[i].delete();  ///close
				}else{
					//排除空图片（无扩展名）
					if(StringUtils.hasText(fileext_snap[i])){
						savefile_snap[i]=new File(dir_snap + filename_snap[i] + "."
								+ fileext_snap[i]);	
						uploadFile_snap[i].write(savefile_snap[i]);	
						uploadFile_snap[i].delete(); ////close
					}
				}
			}
			/************************/
			/* 获取文件上传的路径名称*/
			String fileName_apk = uploadFile_apk.getName();
			String fileName_icon = uploadFile_icon.getName();
			/* 获取文件扩展名 */
		    fileext_apk = fileName_apk.substring(fileName_apk
					.lastIndexOf(".") + 1);
			fileext_icon = fileName_icon.substring(fileName_icon
					.lastIndexOf(".") + 1);
			/* 文件校检 */
			String checkresult_apk = applicationService.fileVertify(
					fileext_apk, uploadFile_apk);
			String checkresult_icon = applicationService.fileVertify(
					fileext_icon, uploadFile_icon);
			if (!checkresult_apk.equals("pass") || !checkresult_icon.equals("pass")) {
				return "{success:false , err: '异常'}";
			}
			/* 重命名文件 */
			filename_apk = UUID.randomUUID().toString();
			filename_icon = UUID.randomUUID().toString();
			savefile_apk = new File(dir_apk + filename_apk + "."
					+ fileext_apk);
			savefile_icon = new File(dir_img + filename_icon + "."
					+ fileext_icon);
			/* 获取文件大 小*/
			filesize_app = uploadFile_apk.getSize();
			/* 存储上传文件 */
			uploadFile_apk.write(savefile_apk);
			uploadFile_apk.delete(); ///close
			uploadFile_icon.write(savefile_icon);
			uploadFile_icon.delete(); ///close
		} catch (Exception ex) {
			/* 清理垃圾文件 */
			applicationService.clear(files);
			logger.error("=err==" + ex.getMessage());
			return "{success:false ,err:文件信息保存失败";
		}
		//==========================以下是入库操作===========================================
		/*分类SEQ*/
		String typeSeq = appTypeService.findEntity(Long.parseLong(typeId)).getTypeSeq();
		/* 保存成功后读取apk包信息 */
		List<String> apkInfos = applicationService.readApkInfo(savefile_apk);
		if(!apkInfos.isEmpty()){
			appPackage = apkInfos.get(1);
			appVer = apkInfos.get(2);
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
		newApp.setTypeId(Long.parseLong(typeId)); //分类
		newApp.setTypeSeq(typeSeq); //分类SEQ
		newApp.setKeyWords(keyWords);
		newApp.setAppWebsite(appWebsite);
		newApp.setSupportEmail(supportEmail);
		newApp.setDownTimes(0);
		newApp.setIconUrl("/resources/apk/images/"+filename_icon+"."+fileext_icon);
		newApp.setFileId(fileId);
		/*APP_PACKAGE相同则认为是同一软件不同版本，GLOBAL_MARK相同*/
		List<Application> apps = applicationService.findByProperty("appPackage", appPackage);		
		if(!apps.isEmpty()){
			//appPackage已经存在
			newApp.setAppPackage(appPackage);
			newApp.setGlobalMark(apps.get(0).getGlobalMark());
			newApp.setVerMark(apps.size()); 
		}else{
			//appPackage不存在
			newApp.setAppPackage(appPackage);
			newApp.setVerMark(0); //版本标识
			newApp.setGlobalMark(UUID.randomUUID().getLeastSignificantBits());  //yy全局标识	
		}		
		newApp.setCommentTimes(0);
		newApp.setSoftLevel((short)0);
		Date d = new Date();
		newApp.setCreateTime(d);
		newApp.setUpdateTime(d);
		if(appVer!=null){  //应用版本--来自AndriodManifest.xml文件
			newApp.setAppVer(appVer);	
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
		//保存截图
		for(int i =0;i<filename_snap.length;i++){
			if(!StringUtils.hasText(fileext_snap[i]))
				continue;
			AppSnap newSnap = new AppSnap();
			newSnap.setAppId(appId);
			newSnap.setFileId(fileId);
			if(snapIndex==i){
				//保存默认的图片
				newSnap.setSnapUrl("/resources/apk/snap/"+filename_snap[i]+"__default"+"."+fileext_snap[i]);//__default
			}else{
				//保存非默认的图片				
				newSnap.setSnapUrl("/resources/apk/snap/"+filename_snap[i]+"."+fileext_snap[i]);				
			}
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
		}
		return "{success:true}";
	}

	
	/************************************************
	 * ①加载app基本信息
	 * ②默认截图放在首位，其他截图往后排
	 * ***********************************************
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/loadApplication", method = RequestMethod.POST)
	@ResponseBody
	public Application loadApplication(Long id) {
		//设置默认截图/snapUrl_default/和其它非默认截图
		Application app = applicationService.findEntity(id);		
		List<AppSnap> listSnap = appSnapService.findByProperty("appId", id);
		int i=1;
		for(AppSnap o :listSnap){
			if(o.getSnapUrl()==null)
				continue;
			if(o.getSnapUrl().contains("__default")){
				app.setSnapUrl(o.getSnapUrl());
			}else if(i==1){
				app.setSnapUrl_1(o.getSnapUrl());
				i++;
			}else if(i==2){
				app.setSnapUrl_2(o.getSnapUrl());
				i++;
			}else if(i==3){
				app.setSnapUrl_3(o.getSnapUrl());
				i++;
			}else if(i==4){
				app.setSnapUrl_4(o.getSnapUrl());
			}
		}
		return app;
	}
	
	
	/************************************************
	 * ①更新app基本信息
	 * ②图片文件路径没有变化
	 * ③apk文件Ver_Mark根据新上传的文件的版本号进行判断
	 * ④图标文件如果重新上传会删除之前保留的，用新上传的图标，否则保留原先的图标
	 * ⑤软件截图只可以选择新建时上传的五张图之一作为默认图片
	 * ***********************************************
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
	@ResponseBody
	public String updateApplication(HttpServletRequest request,
			HttpServletResponse response )throws ServletException, IOException {
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
	    File savefile_apk = null  , savefile_icon =null;
		String id /*主键*/,appName /*名称*/ , appState /*状态*/, keyWords /*关键字*/, typeId /*分类*/,appPackage /*包名*/ = "",appVer= null,/*版本*/
		filename_apk /*apk路径 */ = null,fileext_apk /*扩展名*/ = null,filename_icon /*icon路径 */ = null,fileext_icon = null ,/*扩展名*/dir_snap,/*截图目录*/
		app_sdk_ver /*sdk版本*/,app_author /*作者*/,app_summary /*摘要*/,app_desc,/*描述*/folder/*文件目录*/;
		String snapIndexUrl;/*默认截图初始化*/
		boolean b_apk_update =false, b_icon_update=false; /*apk和图标是否发生改变的标志位*/
		long filesize_app = 0 ; //文件大小
		String[] arr2;
		/*定义文件数组*/
		List<File> files = new ArrayList<File>();
		files.add(savefile_apk);
		files.add(savefile_icon);
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
			/*文件存储在目录中的绝对路径 */
			folder = request.getSession().getServletContext()
					.getRealPath("/");
			String dir_apk = folder + File.separator + "resources\\apk\\upload"
					+ File.separator;
			String dir_img = folder + File.separator + "resources\\apk\\images"
					+ File.separator;
			dir_snap = folder + File.separator + "resources\\apk\\snap"
					+ File.separator;
			/*抽取表单必填信息 */
			id = (String)fields.get("id");  //主键
			appName = (String)fields.get("appName");
			appState = (String)fields.get("appState");
			keyWords = (String)fields.get("keyWords");
			typeId	= (String)fields.get("typeId");
			snapIndexUrl = (String)fields.get("snapIndex"); //获取默认截图拼装信息
			/*非必填信息*/
			app_sdk_ver =(String)fields.get("minSdkVer");
			app_author=(String)fields.get("authorName");
			app_summary = (String)fields.get("appSummary");
			app_desc = (String)fields.get("appDesc");
			
			/*处理apk文件*/
			FileItem uploadFile_apk = (FileItem)fields.get("id_app_apk");
			String fileName_apk = uploadFile_apk.getName();
			if(StringUtils.hasText(fileName_apk)){
				b_apk_update = true;
			    fileext_apk = fileName_apk.substring(fileName_apk
						.lastIndexOf(".") + 1);
				/* 重命名文件 */
				filename_apk = UUID.randomUUID().toString();
				savefile_apk = new File(dir_apk + filename_apk + "."
						+ fileext_apk);
				/* 获取文件大 小*/
				filesize_app = uploadFile_apk.getSize();
				/* 存储上传文件 */
				uploadFile_apk.write(savefile_apk);
				uploadFile_apk.delete();///close
			}
			/*处理图标*/
			FileItem uploadFile_icon = (FileItem) fields.get("id_app_icon");
			String fileName_icon = uploadFile_icon.getName();
			if(StringUtils.hasText(fileName_icon)){
				b_icon_update=true;  //更新				
				fileext_icon = fileName_icon.substring(fileName_icon
						.lastIndexOf(".") + 1);	
				filename_icon = UUID.randomUUID().toString();
				savefile_icon = new File(dir_img + filename_icon + "."
						+ fileext_icon);
				uploadFile_icon.write(savefile_icon);	
				uploadFile_icon.delete();///close
			}			
			/*处理截图*/
			FileItem[] uploadFile_snap =new FileItem[5]; 	//获取截图文件
			String[] fileName_snap = new String[5]; 		//获取截图路径名
			String[] fileext_snap  = new String[5]; //获取截图扩展名
			File[] savefile_snap =  new File[5];
			arr2 = snapIndexUrl.split("#");
			for(int i=0;i<5 ;i++){
				if(fields.get("id_app_snap"+i)==null){  
					continue;
				}
				uploadFile_snap[i] = (FileItem)fields.get("id_app_snap"+i);
				fileName_snap[i] = uploadFile_snap[i].getName();
				fileext_snap[i] = fileName_snap[i].substring(fileName_snap[i].lastIndexOf(".")+1);	
				
				if(StringUtils.hasText(fileext_snap[i])){
					if(arr2[i+2].contains("undefined")){//undefined表示截图原先不存在
						/***处理更新时新上传的截图****/
						String fileanme =  UUID.randomUUID().toString();
						savefile_snap[i]=new File(dir_snap + fileanme + "."
								+ fileext_snap[i]);	
						arr2[1]=arr2[i+2]; //arr2[1]是交换的位置
						uploadFile_snap[i].write(savefile_snap[i]);	
						uploadFile_snap[i].delete();///close
						//保存操作
						AppSnap newSnap = new AppSnap();
						Application updateApp = applicationService.findEntity(Long.parseLong(id));
						Long appId = updateApp.getId();
						Long fileId = updateApp.getFileId();
						newSnap.setAppId(appId);
						newSnap.setFileId(fileId);
						newSnap.setSnapUrl("/resources/apk/snap/"+fileanme+"."+fileext_snap[i]);				
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
					}else{
						/****处理更新时已经上传存在的截图*****/
						String newUri = dir_snap+arr2[i+2].substring(arr2[i+2].lastIndexOf("/")+1);
						savefile_snap[i]= new File(newUri); //i+2表示的是交换前各截图实际位置的索引，i=0和i=1表示的是要交换截图位置的索引
						arr2[1]=arr2[i+2]; //arr2[1]是交换的位置
						uploadFile_snap[i].write(savefile_snap[i]);		
						uploadFile_snap[i].delete();///close
					}
				}
			}
			/////////////////////////////////
			
		} catch (Exception ex) {
			/* 清理垃圾文件 */
			applicationService.clear(files);
			logger.error("=err==" + ex.getMessage());
			return "{success:false ,err:文件信息保存失败";
		}
		//==========================以下是更新时入库操作===========================================
		/*分类SEQ*/
		String typeSeq = appTypeService.findEntity(Long.parseLong(typeId)).getTypeSeq();		
		//TODO:更新默认截图，文件名不变，文件内容交换即可
		if(!snapIndexUrl.contains("DEFAULT_NOT_CHANGE")){
			//String[] arr = snapIndexUrl.split("#");
			String oldUri = dir_snap+arr2[0].substring(arr2[0].lastIndexOf("/")+1);
			String newUri = dir_snap+arr2[1].substring(arr2[1].lastIndexOf("/")+1);
			String temp = dir_snap+File.separator+"temp"+".jpg";
			File ftemp = new File(temp);
			File fold= new File(oldUri);
			File fnew = new File(newUri);
			FileUtils.copyFile(fold, ftemp);
			FileUtils.copyFile(fnew,fold);
			FileUtils.copyFile(ftemp, fnew);
			FileUtils.forceDelete(ftemp);
		}
		/* 保存成功后读取apk包信息 */
		if(b_apk_update){
			List<String> apkInfos = applicationService.readApkInfo(savefile_apk);
			if(!apkInfos.isEmpty()){
				appPackage = apkInfos.get(1);
				appVer = apkInfos.get(2);
			}	
		}
		/*更新应用包*/
		Application updateApp = applicationService.findEntity(Long.parseLong(id));
		Long appFileId = updateApp.getFileId();
		/*更新应用文件 */
		if(b_apk_update){
			AppFile updateAppfile =appFileService.findEntity(appFileId);
			String oldapk = folder+updateAppfile.getFilepath();
			updateAppfile.setFileName(filename_apk+"."+fileext_apk);
			updateAppfile.setFilesize(new Integer((int)filesize_app));
			updateApp.setFileSize(new Float(filesize_app));		
			/*APP_PACKAGE相同则认为是同一软件不同版本，GLOBAL_MARK相同，更新时GLOBAL_MARK不变*/
			List<Application> apps = applicationService.findByProperty("appPackage", appPackage);		
			if(!apps.isEmpty()){
				//appPackage已经存在
				updateApp.setAppPackage(appPackage);
				updateApp.setGlobalMark(apps.get(0).getGlobalMark());
				updateApp.setVerMark(apps.size()); 
			}else{
				//appPackage不存在
				updateApp.setAppPackage(appPackage);
				updateApp.setVerMark(0); //版本标识
				updateApp.setGlobalMark(UUID.randomUUID().getLeastSignificantBits());  //yy全局标识	
			}	
			if(appVer!=null){  //应用版本--来自AndriodManifest.xml文件
				updateApp.setAppVer(appVer);	
			}
			//删除旧的apk文件
			File old = new File(oldapk);
			if(old.delete()){
				updateAppfile.setFilepath("/resources/apk/upload/"+filename_apk+"."+fileext_apk);
			}
			appFileService.updateEntity(updateAppfile);
		}
		/*更新应用软件*/
		updateApp.setMinSdkVer(app_sdk_ver);
		updateApp.setAuthorName(app_author);
		updateApp.setAppSummary(app_summary);
		updateApp.setAppDesc(app_desc);
		updateApp.setAppName(appName);
		updateApp.setAppState(Short.valueOf(appState));
		updateApp.setTypeId(Long.parseLong(typeId));
		updateApp.setTypeSeq(typeSeq);
		updateApp.setKeyWords(keyWords);
		if(b_icon_update){ //图片更新时
			//删除旧图标
			String originalIconUrl = folder+updateApp.getIconUrl();
			File file = new File(originalIconUrl);
			if(file.exists()){
				if(file.delete())
					updateApp.setIconUrl("/resources/apk/images/"+filename_icon+"."+fileext_icon);
			}
		}
		updateApp.setCommentTimes(0);
		updateApp.setSoftLevel((short)0);
		Date d = new Date();
		updateApp.setUpdateTime(d);
		applicationService.updateEntity(updateApp);
		return "{success:true}";
	}

	
	/************************************************
	 * ①删除app基本信息
	 * ②删除apk文件、图标、截图等
	 * ③删除 APP_E_FILE、APP_J_SCREENSHOT、APP_E_APP表中残余信息
	 * ***********************************************
	 * @throws ServletException
	 * @throws IOException   文件id不存在、图标id不存在
	 */
	@RequestMapping(value = "/deleteApplication", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteApplication(Long id) {
		Application app = applicationService.findEntity(id);
		String folder = this.servletContext.getRealPath("/");
		//del file
		String app_file = appFileService.findEntity(app.getFileId()).getFilepath();
		File appfile = new File(folder+app_file);
		if(appfile.exists()){
			appfile.delete();	
		}
		appFileService.deleteEntity(app.getFileId());
		//del icon
		File appIcon = new File(folder+app.getIconUrl());
		if(appIcon.exists()){
			appIcon.delete();
		}
		//del snaps
		List<AppSnap> snapList = appSnapService.findByProperty("appId", id);
		for(AppSnap o : snapList){
			File aFile = new File(folder+o.getSnapUrl());
			if(aFile.exists()){
				aFile.delete();
			}
			appSnapService.deleteEntity(o.getId());
		}			
		//删除推荐关联信息		
		List<AppRecoRs> list = 	appRecoRsService.findByProperty("appId",id);
		if(!list.isEmpty()){
			for(AppRecoRs o : list){
				appRecoRsService.deleteEntity(o.getId());
			}	
		}
		applicationService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	//下载次数累计
	@RequestMapping(value = "/updateAppDownTimes", method = RequestMethod.POST)
	@ResponseBody
	public String updateAppDownTimes(Long appId) {
		Application app = applicationService.findEntity(appId);
		app.setDownTimes(app.getDownTimes().intValue() + 1);
		applicationService.createOrUpdate(app);
		return ResponseData.SUCCESS_NO_DATA.toString();
	}
	 
	//发布状态变更
	@RequestMapping(value = "/distApp", method = RequestMethod.POST)
	@ResponseBody
	public String distApp(Long appId) {
		Application app = applicationService.findEntity(appId);
		app.setAppState((short)1);
		applicationService.createOrUpdate(app);
		return ResponseData.SUCCESS_NO_DATA.toString();
	}
	
	 
	//应用推荐或取消推荐
	@RequestMapping(value = "/recommandOper", method = RequestMethod.POST)
	@ResponseBody
	public String recommandOper(AppRecoRs apprecors) {
		if(apprecors.getRecoId()==null){
			//取消推荐
			List<AppRecoRs> list = 	appRecoRsService.findByProperty("appId",apprecors.getAppId());
			if(!list.isEmpty()){
				for(AppRecoRs o : list){
					appRecoRsService.deleteEntity(o.getId());
				}	
			}
		}else{
			//推荐
			Application app = applicationService.findEntity(apprecors.getAppId());
			apprecors.setGlobalMark(app.getGlobalMark());
			apprecors.setCreateTime(app.getCreateTime());
			apprecors.setUpdateTime(app.getUpdateTime());
			//apprecors.setOrderNum(orderNum); //顺序 ?
			appRecoRsService.insertEntity(apprecors);	
		}
		return ResponseData.SUCCESS_NO_DATA.toString();
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext; 
	}
	
}
