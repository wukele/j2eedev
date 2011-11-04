package com.iteye.melin.web.model.support;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * AppEApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_c_type")
public class AppType {
	private Long id;
	private String typeName;
	private Integer parentT;
	private String typeSeq;
	private String iconUrl;
	private Date createTime;
	private Date updateTime;

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "TYPE_ID")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TYPE_NAME")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Column(name = "PARENT_TYPE_ID")
	public Integer getParentT() {
		return parentT;
	}

	public void setParentT(Integer parentT) {
		this.parentT = parentT;
	}
	@Column(name = "TYPE_SEQ")
	public String getTypeSeq() {
		return typeSeq;
	}

	public void setTypeSeq(String typeSeq) {
		this.typeSeq = typeSeq;
	}
	@Column(name = "ICON_URL", length = 128)
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}