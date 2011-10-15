package com.iteye.melin.web.model.base;

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
@Table(name = "app_e_app")
public class Application {
	private Long appId;
	private String appName;
	private String appVer;
	private Integer typeId;
	private String typeSeq;
	private String minSdkVer;
	private String appPackage;
	private Float fileSize;
	private Integer authorId;
	private String authorName;
	private String appWebsite;
	private String supportEmail;
	private String keyWords;
	private String appSummary;
	private String appDesc;
	private Short softLevel;
	private Short appState;
	private Integer downTimes;
	private Integer commentTimes;
	private Long iconFileId;
	private String iconUrl;
	private Long fileId;
	private Long globalMark;
	private Integer verMark;
	private Date createTime;
	private Date updateTime;

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "APP_ID")
	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Column(name = "APP_NAME")
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "APP_VER")
	public String getAppVer() {
		return this.appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	@Column(name = "TYPE_ID")
	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@Column(name = "TYPE_SEQ")
	public String getTypeSeq() {
		return this.typeSeq;
	}

	public void setTypeSeq(String typeSeq) {
		this.typeSeq = typeSeq;
	}

	@Column(name = "MIN_SDK_VER")
	public String getMinSdkVer() {
		return this.minSdkVer;
	}

	public void setMinSdkVer(String minSdkVer) {
		this.minSdkVer = minSdkVer;
	}

	@Column(name = "APP_PACKAGE")
	public String getAppPackage() {
		return this.appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	@Column(name = "FILE_SIZE")
	public Float getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Float fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "AUTHOR_ID")
	public Integer getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Column(name = "APP_WEBSITE")
	public String getAppWebsite() {
		return this.appWebsite;
	}

	public void setAppWebsite(String appWebsite) {
		this.appWebsite = appWebsite;
	}

	@Column(name = "SUPPORT_EMAIL")
	public String getSupportEmail() {
		return this.supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	@Column(name = "KEY_WORDS")
	public String getKeyWords() {
		return this.keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	@Column(name = "APP_SUMMARY")
	public String getAppSummary() {
		return this.appSummary;
	}

	public void setAppSummary(String appSummary) {
		this.appSummary = appSummary;
	}

	@Column(name = "APP_DESC")
	public String getAppDesc() {
		return this.appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	@Column(name = "SOFT_LEVEL")
	public Short getSoftLevel() {
		return this.softLevel;
	}

	public void setSoftLevel(Short softLevel) {
		this.softLevel = softLevel;
	}

	@Column(name = "APP_STATE")
	public Short getAppState() {
		return this.appState;
	}

	public void setAppState(Short appState) {
		this.appState = appState;
	}

	@Column(name = "DOWN_TIMES")
	public Integer getDownTimes() {
		return this.downTimes;
	}

	public void setDownTimes(Integer downTimes) {
		this.downTimes = downTimes;
	}

	@Column(name = "COMMENT_TIMES")
	public Integer getCommentTimes() {
		return this.commentTimes;
	}

	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}

	@Column(name = "ICON_FILE_ID")
	public Long getIconFileId() {
		return this.iconFileId;
	}

	public void setIconFileId(Long iconFileId) {
		this.iconFileId = iconFileId;
	}

	@Column(name = "ICON_URL", length = 128)
	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Column(name = "FILE_ID")
	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "GLOBAL_MARK")
	public Long getGlobalMark() {
		return this.globalMark;
	}

	public void setGlobalMark(Long globalMark) {
		this.globalMark = globalMark;
	}

	@Column(name = "VER_MARK")
	public Integer getVerMark() {
		return this.verMark;
	}

	public void setVerMark(Integer verMark) {
		this.verMark = verMark;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}