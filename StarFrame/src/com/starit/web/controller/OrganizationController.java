package com.starit.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.util.ResponseData;
import com.starit.core.util.SQLOrderMode;
import com.starit.web.model.Organization;
import com.starit.web.service.OrganizationService;

/**
 *
 * @datetime 2010-8-17 下午09:59:59
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/org")
public class OrganizationController {
	//~ Instance fields ================================================================================================
	@Autowired
	private OrganizationService organizationService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/org";
	}
	
	/**
	 * 根据父机构ID，查找所有子机构信息
	 * 
	 * @param node 父机构ID
	 * @return 所有子机构
	 */
	@RequestMapping("/findOrgs")
	@ResponseBody
	public List<Organization> findMenus(Long node) {
		if(node == null)
			node = 0L;
		return organizationService.findByPropertyAndOrder("parentId", node, "theSort", SQLOrderMode.ASC);
	}
	
	/**
	 * 保存指定节点下，所有直接子节点的顺序。
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID有逗号连接的字符串
	 * @return
	 */
	@RequestMapping("/saveOrgOrder")
	@ResponseBody
	public ResponseData saveOrganizationOrder(Long parentId, String childIds) {
		organizationService.saveOrganizationOrder(parentId, childIds);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 插入机构信息
	 * 
	 * @param organization
	 * @return
	 */
	@RequestMapping(value="/insertOrg", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertOrganization(Organization organization) {
		organizationService.insertEntity(organization);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新机构信息
	 * 
	 * @param organization
	 * @return
	 */
	@RequestMapping(value="/updateOrg", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateOrganization(Organization organization) {
		organizationService.updateEntity(organization);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载机构信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/loadOrg", method=RequestMethod.POST)
	@ResponseBody
	public Organization loadOrganization(Long id) {
		return organizationService.findEntity(id);
	}
	
	/**
	 * 删除机构
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID有逗号连接的字符串
	 * @return
	 */
	@RequestMapping("/deleteOrg")
	@ResponseBody
	public ResponseData deleteOrganization(long id) {
		organizationService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
