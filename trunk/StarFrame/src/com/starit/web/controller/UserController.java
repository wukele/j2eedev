package com.starit.web.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.util.ResponseData;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.model.Role;
import com.starit.web.model.User;
import com.starit.web.service.UserService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/user")
public class UserController {
	//~ Instance fields ================================================================================================
	@Autowired
	private UserService userService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/user";
	}
	
	/**
	 * 用户管理，查询用户信息，按照用户优先级降序排序
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryUsers")
	@ResponseBody
	public Page<User> pageQueryResources(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, User user, String sort, String dir) {
		PageRequest<User> pageRequest = new PageRequest<User>(pageNumber, pageSize);
		pageRequest.getJoinEntitys().add("employee");
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(user.getOperatorName()))
			likeFilters.put("operatorName", user.getOperatorName());

		return userService.findAllForPage(pageRequest);
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
		userService.insertEntity(user);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新用户, 只接受POST请求
	 * 
	 * @param user
	 * @return ResponseData
	 */
	@RequestMapping(value="/updateUser", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateResource(User user) {
		userService.updateEntity(user);
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
	
	/**
	 * 查询用户关联的所有角色, 只接受POST请求
	 * 
	 * @param operatorId 用户ID
	 * @return List<Role> 用户关联的所有角色
	 */
	@RequestMapping(value="/queryRoles4User", method=RequestMethod.POST)
	@ResponseBody
	public Set<GrantedAuthority> queryRoles4User(Long operatorId) {
		return userService.queryRoles4User(operatorId);
	}
	
	/**
	 * 查询所有用户信息，同时关联查询指定的角色信息
	 *
	 * @param operatorId
	 */
	@RequestMapping(value="/pageQueryRoles4User", method=RequestMethod.POST)
	@ResponseBody
	public Page<Role> pageQueryRoles4User(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Long operatorId)  {
		PageRequest<Role> pageRequest = new PageRequest<Role>(pageNumber, pageSize);
		return userService.pageQueryRoles4User(pageRequest, operatorId);
	}
	
	/**
	 * 给用户绑定角色, 只接受POST请求
	 * 
	 * @param operatorId 用户ID
	 * @param ids 角色ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/bindRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData bindRole(Long operatorId, String ids) {
		userService.bindRole(operatorId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 给用户取消绑定角色, 只接受POST请求
	 * 
	 * @param operatorId 用户ID
	 * @param ids 角色ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/unBindRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData unBindRole(Long operatorId, String ids) {
		userService.unBindRole(operatorId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
