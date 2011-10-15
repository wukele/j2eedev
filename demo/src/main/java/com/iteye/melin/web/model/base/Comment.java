package com.iteye.melin.web.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="APP_E_COMMENT")
public class Comment {
	@Id
	@Column(name="COMMENT_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	
	@Column(name="APP_ID")
	private Long appId;
	@Formula("(select a.APP_NAME from app_e_app a where a.APP_ID= APP_ID)")
	private String appName;
	
	@Column(name="APP_VER")
	private String appVer;
	
	@Column(name="GLOBAL_MARK")
	private Long globalMark;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="USER_NICKNAME")
	private String userNickname;
	
	@Column(name="SOFT_LEVEL")
	private int softLevel;
	
	@Column(name="COMMENT_DETAIL")
	private String detail;
	
	@Column(name="IP_ADDR")
	private String ipAddr;
	
	@Column(name="CREATE_TIME")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public Long getGlobalMark() {
		return globalMark;
	}

	public void setGlobalMark(Long globalMark) {
		this.globalMark = globalMark;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public int getSoftLevel() {
		return softLevel;
	}

	public void setSoftLevel(int softLevel) {
		this.softLevel = softLevel;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
