package com.starit.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

import com.starit.core.web.model.BaseEntity;

@Entity
@Table(name="ST_ORGANIZATION")
public class Organization extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int level;
	private String name;
	private long parentId;
	private int theSort;
	private String orgSeq;
	private String descn;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getOrgSeq() {
		return orgSeq;
	}
	public void setOrgSeq(String orgSeq) {
		this.orgSeq = orgSeq;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public int getTheSort() {
		return theSort;
	}
	public void setTheSort(int theSort) {
		this.theSort = theSort;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}