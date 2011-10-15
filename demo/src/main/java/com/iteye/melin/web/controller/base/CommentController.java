package com.iteye.melin.web.controller.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Comment;
import com.iteye.melin.web.service.base.CommentService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private CommentService commentService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/comment";
	}

	@RequestMapping("/pageQueryComments")
	@ResponseBody
	public Page<Comment> pageQueryComments(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Comment comment, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<Comment> pageRequest = new PageRequest<Comment>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		
//		if(StringUtils.hasText(user.getUserCode()))
//			pageRequest.getFilters().put("userCode", user.getUserCode());

		Page<Comment> page = commentService.findAllForPage(pageRequest);
		return page;
	}
	
	@RequestMapping(value="/insertComment", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertComment(Comment comment) {
		comment.setCreateTime(new Date());

        commentService.insertEntity(comment);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadComment", method=RequestMethod.POST)
	@ResponseBody
	public Comment loadComment(Long id) {
		return commentService.findEntity(id);
	}
	
	@RequestMapping(value="/updateComment", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateComment(Comment comment) {
		commentService.updateEntity(comment);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value="/deleteComment", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		commentService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
