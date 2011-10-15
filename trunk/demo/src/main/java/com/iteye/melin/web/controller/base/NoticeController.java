package com.iteye.melin.web.controller.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Notice;
import com.iteye.melin.web.model.base.User;
import com.iteye.melin.web.service.base.NoticeService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private NoticeService noticeService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/notice";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

	@RequestMapping("/pageQueryNotices")
	@ResponseBody
	public Page<Notice> pageQueryNotices(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Notice notice, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<Notice> pageRequest = new PageRequest<Notice>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(notice.getTitle()))
			likeFilters.put("title", notice.getTitle());
		
//		if(StringUtils.hasText(user.getUserCode()))
//			pageRequest.getFilters().put("userCode", user.getUserCode());

		Page<Notice> page = noticeService.findAllForPage(pageRequest);
		return page;
	}
	
	@RequestMapping(value="/insertNotice", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertNotice(HttpServletRequest request, Notice notice) {
		notice.setCreateTime(new Date());
		
		HttpSession session = request.getSession();
        User user = (User)session.getAttribute("__SESSIONKEY__");
        
        notice.setCreator(user.getUserName());
		noticeService.insertEntity(notice);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadNotice", method=RequestMethod.POST)
	@ResponseBody
	public Notice loadNotice(Long id) {
		return noticeService.findEntity(id);
	}
	
	@RequestMapping(value="/updateNotice", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateNotice(Notice notice) {
		noticeService.updateEntity(notice);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value="/deleteNotice", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		noticeService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
