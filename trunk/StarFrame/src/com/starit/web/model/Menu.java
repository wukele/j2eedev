package com.starit.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

import com.starit.core.web.model.BaseEntity;

/**
 *
 * @datetime 2010-8-8 下午04:39:04
 * @author libinsong1204@gmail.com
 */
@Entity
@Table(name="ST_MENU")
public class Menu extends BaseEntity {
	private static final long serialVersionUID = 1L;
	//~ Instance fields ================================================================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(length=1, columnDefinition="char")
	private String isLeaf;
	private int level;
	private Long parentId;
	private int theSort;
	private String iconCls;
	private String menuSeq;
	private String descn;
	
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "resource_id")
	private Resource resource;
	
	//~ Methods ========================================================================================================
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@JsonProperty("text")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setText(String text) {
		this.name = text;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	@JsonProperty
	public boolean isLeaf() {
		if("N".equals(isLeaf))
			return false;
		else
			return true;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public int getTheSort() {
		return theSort;
	}
	public void setTheSort(int theSort) {
		this.theSort = theSort;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getMenuSeq() {
		return menuSeq;
	}
	public void setMenuSeq(String menuSeq) {
		this.menuSeq = menuSeq;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
}
