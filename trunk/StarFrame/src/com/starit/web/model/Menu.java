package com.starit.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.starit.core.web.model.BaseEntity;

/**
 *
 * @datetime 2010-8-8 下午04:39:04
 * @author libinsong1204@gmail.com
 */
@Entity
@Table(name="ST_MENU")
public class Menu extends BaseEntity {
	//~ Instance fields ================================================================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String text;
	private boolean leaf;
	private String url;
	private int level;
	private long parentId;
	private int theSort;
	private String iconCls;
	private String menuSeq;
	private String descn;
	
	//~ Methods ========================================================================================================
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
