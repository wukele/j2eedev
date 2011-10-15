package com.iteye.melin.web.controller.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/recommend";
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

		Page<Recommend> page = recommendService.findAllForPage(pageRequest);
		DictionaryHolder.transfercoder(page.getResult(), 10008L, "getType");
		return page;
	}
	
	@RequestMapping(value="/insertRecommend", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertRecommend(Recommend recommend) {
		recommend.setCreateTime(new Date());
        recommendService.insertEntity(recommend);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadRecommend", method=RequestMethod.POST)
	@ResponseBody
	public Recommend loadRecommend(Long id) {
		return recommendService.findEntity(id);
	}
	
	@RequestMapping(value="/updateRecommend", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateRecommend(Recommend recommend) {
		recommend.setUpdateTime(new Date());
		recommendService.updateEntity(recommend);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value="/deleteRecommend", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		recommendService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
