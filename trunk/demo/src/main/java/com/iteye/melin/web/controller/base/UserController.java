package com.iteye.melin.web.controller.base;

import java.util.Map;

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
import com.iteye.melin.web.model.base.User;
import com.iteye.melin.web.service.base.UserService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private UserService userService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/user";
	}
	
	/**
	 * 用户管理，查询用户信息，按照用户优先级降序排序
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryUsers")
	@ResponseBody
	public Page<User> pageQueryUsers(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, User user, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<User> pageRequest = new PageRequest<User>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(user.getUserName()))
			likeFilters.put("userName", user.getUserName());
		
//		if(StringUtils.hasText(user.getUserCode()))
//			pageRequest.getFilters().put("userCode", user.getUserCode());

		Page<User> page = userService.findAllForPage(pageRequest);
		return page;
	}
	
	/**
	 * 保存用户, 只接受POST请求
	 * 
	 * @param user
	 * @return ResponseData
	 */
	@RequestMapping(value="/insertUser", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertUser(User user) {
		//初始密码：000000
		userService.insertEntity(user);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载用户, 只接受POST请求
	 * 
	 * @param id
	 * @return User
	 */
	@RequestMapping(value="/loadUser", method=RequestMethod.POST)
	@ResponseBody
	public User loadUser(Long id) {
		return userService.findEntity(id);
	}
	
	/**
	 * 删除用户, 只接受POST请求
	 * 
	 * @param id
	 * @return ResponseData
	 */
	@RequestMapping(value="/deleteUser", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		userService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
