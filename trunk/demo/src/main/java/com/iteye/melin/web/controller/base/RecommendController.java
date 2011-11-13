package com.iteye.melin.web.controller.base;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.DictionaryHolder;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Recommend;
import com.iteye.melin.web.service.base.RecommendService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/recommend")
public class RecommendController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private RecommendService recommendService;
	private String rootpath = "C:/images/";
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/recommend";
	}
	
	@RequestMapping("/zhuanti")
	public String zhuanti(){
		return "admin/base/zhuanti";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

	@RequestMapping("/pageQueryRecommends")
	@ResponseBody
	public Page<Recommend> pageQueryRecommends(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Recommend recommend, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<Recommend> pageRequest = new PageRequest<Recommend>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(recommend.getName()))
			likeFilters.put("name", recommend.getName());
		
		if(recommend.getType() > 0)
			pageRequest.getFilters().put("type", recommend.getType());

		pageRequest.setExtraCondition(" type in (1, 2)");
		Page<Recommend> page = recommendService.findAllForPage(pageRequest);
		DictionaryHolder.transfercoder(page.getResult(), 10008L, "getType");
		/*//查询全部专题
		if(recommend.getType()==4){
			Recommend r = new Recommend();
			r.setId(new Long(-2));
			r.setName("全部专题");
			r.setType(4);
			page.getResult().add(r);
		}*/
		return page;
	}
	
	@RequestMapping("/pageQueryRecommends1")
	@ResponseBody
	public Page<Recommend> pageQueryRecommends1(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Recommend recommend, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir , @RequestParam(defaultValue = "true",required = false )boolean special) {
		PageRequest<Recommend> pageRequest = new PageRequest<Recommend>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(recommend.getName()))
			likeFilters.put("name", recommend.getName());
		
		pageRequest.getFilters().put("type", 4);
		Page<Recommend> page = recommendService.findAllForPage(pageRequest);
		if(!special){
			//查询全部专题{{
			Recommend r = new Recommend();
			r.setId(new Long(-2));
			r.setName("全部专题");
			r.setType(4);
			page.getResult().add(r);
		//查询无专题{{
			Recommend r2 = new Recommend();
			r2.setId(new Long(-1));
			r2.setName("无专题");
			r2.setType(4);
			page.getResult().add(r2);
		//}}	
		}
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/insertRecommend", method=RequestMethod.POST)
	@ResponseBody
	public String  insertRecommend(/*@RequestParam MultipartFile file, Recommend recommend*/HttpServletRequest request) throws IOException {
//		recommend.setCreateTime(new Date());
//		
//		if(!file.isEmpty()) {
//			String path = rootpath + file.getOriginalFilename();
//			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path));
//			recommend.setPosterUrl(path);
//		}
//        recommendService.insertEntity(recommend);
		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("utf-8");
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
			FileItem file = (FileItem)fields.get("file");
			String path = rootpath + file.getName();
			File updateFile = new File(path);	
			boolean filecreated = true;
			if(!updateFile.exists()){
				filecreated = updateFile.createNewFile();
			}
			if(filecreated){
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path));
				file.delete();
				String linkUrl = (String)fields.get("linkUrl");
				String type = (String)fields.get("type");
				Recommend recommend = new Recommend();
				recommend.setCreateTime(new Date());
				recommend.setUpdateTime(new Date());
				recommend.setType(Integer.parseInt(type));				
				recommend.setLinkUrl(linkUrl);
				recommend.setPosterUrl(path);
				recommendService.insertEntity(recommend);	
			}			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return "{success:true}";
	}
	
	@RequestMapping(value="/insertRecommend1", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertRecommend1(Recommend recommend) throws IOException {
		recommend.setCreateTime(new Date());
		recommend.setType(4);
        recommendService.insertEntity(recommend);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadRecommend", method=RequestMethod.POST)
	@ResponseBody
	public Recommend loadRecommend(Long id) {
		return recommendService.findEntity(id);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateRecommend", method=RequestMethod.POST)
	@ResponseBody
	public String updateRecommend(/*@RequestParam MultipartFile file, Recommend recommend*/HttpServletRequest request) throws IOException {
//		Recommend tmp = recommendService.findEntity(recommend.getId());
//		tmp.setUpdateTime(new Date());
//		tmp.setLinkUrl(recommend.getLinkUrl());
		
//	if(!file.isEmpty()) {
//		String path = rootpath + file.getOriginalFilename();
//		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path));
//		tmp.setPosterUrl(path);
//	}
		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("utf-8");
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
			FileItem file = (FileItem)fields.get("file");
			String path = rootpath + file.getName();
			File updateFile = new File(path);	
			boolean filecreated = true;
			if(!updateFile.exists()){
				filecreated = updateFile.createNewFile();
			}
			if(filecreated){
				//file.write(new File(path));
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path));
				file.delete();  //删除临时文件
				String id = (String)fields.get("id");
				String linkUrl = (String)fields.get("linkUrl");
				Recommend tmp = recommendService.findEntity(Long.parseLong(id));
				tmp.setUpdateTime(new Date());
				tmp.setLinkUrl(linkUrl);
				tmp.setPosterUrl(path);
				recommendService.updateEntity(tmp);	
			}			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "{success:true}";
	}
	
	@RequestMapping(value="/updateRecommend1", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateRecommend1(Recommend recommend) throws IOException {
		recommend.setUpdateTime(new Date());
		recommend.setType(4);

		recommendService.updateEntity(recommend);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value="/deleteRecommend", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteRecommend(Long id) {
		recommendService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
