package com.iteye.melin.web.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="APP_E_RECOMMEND")
public class Recommend {
	@Id
	@Column(name="RECOMMEND_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	
	@Column(name="RECOMMEND_NAME")
	private String name;
	
	@Column(name="RECOMMEND_TYPE")
	private int type;
	@Transient
	private String type_Name;
	
	@Column(name="LINK_URL")
	private String linkUrl;
	
	@Column(name="POSTER_ID")
	private Long posterId;
	
	@Column(name="POSTER_URL")
	private String posterUrl;
	
	@Column(name="RECOMMEND_DESC")
	private String desc;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="UPDATE_TIME")
	private Date updateTime;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Long getPosterId() {
		return posterId;
	}

	public void setPosterId(Long posterId) {
		this.posterId = posterId;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getType_Name() {
		return type_Name;
	}

	public void setType_Name(String type_Name) {
		this.type_Name = type_Name;
	}
}
