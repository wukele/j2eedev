package com.iteye.melin.web.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="APP_E_NOTICE")
public class Notice {
	@Id
	@Column(name="NOTICE_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	
	@Column(name="NOTICE_TITLE")
	private String title;
	
	@Column(name="NOTICE_CONTENT")
	private String content;
	
	@Column(name="CREATOR")
	private String creator;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="EXPIRED_TIME")
	private Date expiredTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}
}
