package com.iteye.melin.web.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

/**
*
* @datetime 2010-8-20 下午10:09:06
* @author libinsong1204@gmail.com
*/
@Entity
@Table(name="ST_DICTIONARY")
public class Dictionary {
	//~ Instance fields ================================================================================================
	@Id
	@Column(name="RES_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	private String code;
	private String name;
	@Column(name="ORDER_INDEX")
	private Long orderIndex;
	private char enabled;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=DictionaryType.class)   
	@JoinColumn(name="DICT_TYPE_ID", nullable=false)      
	private DictionaryType businType;

	//~ Methods ========================================================================================================
	
	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

	public char getEnabled() {
		return enabled;
	}

	public void setEnabled(char enabled) {
		this.enabled = enabled;
	}

	@JSONField(serialize=false)
	public DictionaryType getBusinType() {
		return businType;
	}

	public void setBusinType(DictionaryType businType) {
		this.businType = businType;
	}	
}
