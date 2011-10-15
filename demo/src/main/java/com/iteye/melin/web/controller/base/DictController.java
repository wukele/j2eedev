package com.iteye.melin.web.controller.base;

import java.util.List;
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
import com.iteye.melin.core.util.DictionaryHolder;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Dictionary;
import com.iteye.melin.web.model.base.DictionaryType;
import com.iteye.melin.web.service.base.DictionaryService;
import com.iteye.melin.web.service.base.DictionaryTypeService;

/**
 * 业务字典Controller
 * 
 * @datetime 2010-8-19 下午10:01:44
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

	//~ Instance fields ================================================================================================
	@Autowired
	private DictionaryTypeService dictService;
	@Autowired
	private DictionaryService dictionaryService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/dict";
	}
	
	/**
	 * 字典管理，查询字典业务类型信息
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryDictBusinTypes")
	@ResponseBody
	public Page<DictionaryType> pageQueryDictBusinTypes(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, DictionaryType businType, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<DictionaryType> pageRequest = new PageRequest<DictionaryType>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(businType.getName()))
			likeFilters.put("name", businType.getName());
			
		return dictService.findAllForPage(pageRequest);
	}
	
	/**
	 * 保存业务字典类型, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/insertDictBusinType", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertDictBusinType(DictionaryType businType) {
		dictService.insertEntity(businType);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新字典业务类型, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/updateDictBusinType", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateDictBusinType(DictionaryType businType) {
		dictService.updateEntity(businType);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载字典业务类型, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/loadDictBusinType", method=RequestMethod.POST)
	@ResponseBody
	public DictionaryType loadDictBusinType(Long id) {
		return dictService.findEntity(id);
	}
	
	/**
	 * 删除字典业务类型, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/deleteDictBusinType", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteDictBusinType(Long id) {
		DictionaryHolder.cleanDictionaries(id);
		dictService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}

	//---------------------------------------字典项-------------------------------------------
	
	/**
	 * 根据父字典项ID和字典业务类型ID，查找所有子父字典项信息
	 * 
	 * @param dictTypeId 
	 * @return 所有子字典项
	 */
	@RequestMapping("/queryDictionarys")
	@ResponseBody
	public List<Dictionary> queryDictionarys(Long dictTypeId) {
		return dictionaryService.queryDictionarys(dictTypeId);
	}
	
	/**
	 * 插入字典项信息
	 * 
	 * @param dictionary
	 * @return
	 */
	@RequestMapping(value="/insertDictionary", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertDictionary(Dictionary dictionary) {
		dictionaryService.insertEntity(dictionary);
		
		DictionaryHolder.cleanDictionaries(dictionary.getBusinType().getId());
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新字典项信息
	 * 
	 * @param dictionary
	 * @return
	 */
	@RequestMapping(value="/updateDictionary", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateDictionary(Dictionary dictionary) {
		dictionaryService.updateEntity(dictionary);
		
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载字典项信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/loadDictionary", method=RequestMethod.POST)
	@ResponseBody
	public Dictionary loadDictionary(Long id) {
		return dictionaryService.findEntity(id);
	}
	
	/**
	 * 删除字典项
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID有逗号连接的字符串
	 * @return
	 */
	@RequestMapping("/deleteDictionary")
	@ResponseBody
	public ResponseData deleteDictionary(Long id) {
		dictionaryService.deleteEntity(id);
		
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 删除一个或者多个字典项, 只接受POST请求
	 * 
	 * @param ids
	 * @return ResponseData
	 */
	@RequestMapping(value="/deleteDictionarys", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteEntityAttrs(Long[] ids) {
		for(Long id:ids){
			dictionaryService.deleteEntity(id);
		}
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 查询字典类型的字典属性
	 * 
	 * @param dictTypeId 字典类型ID
	 * @return
	 */
	@RequestMapping("/pageQueryDict")
	@ResponseBody
	public Page<Dictionary> pageQueryDict(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Long dictTypeId, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<Dictionary> pageRequest = new PageRequest<Dictionary>(startIndex, pageSize);
		if(dictTypeId != null)
			pageRequest.getFilters().put("DICT_TYPE_ID", dictTypeId);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Page<Dictionary> page = dictionaryService.findAllForPage(pageRequest);
		return page;
	}
}
