package com.iteye.melin.web.model.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

/**
*
* @datetime 2010-8-20 下午10:09:06
* @author libinsong1204@gmail.com
*/
@Entity
@Table(name="ST_DICTIONARY_TYPE")
public class DictionaryType {
	//~ Instance fields ================================================================================================
	@Id
	@Column(name="RES_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	private String name;
	private String memo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "businType", fetch = FetchType.LAZY)   
	@OrderBy("orderIndex ASC")
	private List<Dictionary> dictionaries = new ArrayList<Dictionary>();
	
	//~ Methods ========================================================================================================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@JSONField(serialize=false)
	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}
	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
}