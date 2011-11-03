package com.iteye.melin.web.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * AppEApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_e_app")
public class Application {
	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "APP_ID")
	private Long id;
	@Column(name = "APP_NAME")
	private String appName;
	@Column(name = "APP_VER")
	private String appVer;
	@Column(name = "TYPE_ID")
	private Integer typeId;
	@Transient
	private String typeId_Name;
	@Transient
	private String appState_Name;
	@Transient
	private String dlClient;
	@Transient
	private String snapUrl_default;  //默认截图
	@Transient
	private String snapUrl_1;// 截图1
	@Transient
	private String snapUrl_2;//截图2
	@Transient
	private String snapUrl_3;//截图3
	@Transient
	private String snapUrl_4;//截图4
	@Column(name = "TYPE_SEQ")
	private String typeSeq;
	@Column(name = "MIN_SDK_VER")
	private String minSdkVer;
	@Column(name = "APP_PACKAGE")
	private String appPackage;
	@Column(name = "FILE_SIZE")
	private Float fileSize;
	@Column(name = "AUTHOR_ID")
	private Integer authorId;
	@Column(name = "AUTHOR_NAME")
	private String authorName;
	@Column(name = "APP_WEBSITE")
	private String appWebsite;
	@Column(name = "SUPPORT_EMAIL")
	private String supportEmail;
	@Column(name = "KEY_WORDS")
	private String keyWords;
	@Column(name = "APP_SUMMARY")
	private String appSummary;
	@Column(name = "APP_DESC")
	private String appDesc;
	@Column(name = "SOFT_LEVEL")
	private Short softLevel;
	@Column(name = "APP_STATE")
	private Short appState;
	@Column(name = "DOWN_TIMES")
	private Integer downTimes;
	@Column(name = "COMMENT_TIMES")
	private Integer commentTimes;
	@Column(name = "ICON_FILE_ID")
	private Long iconFileId;
	@Column(name = "ICON_URL", length = 128)
	private String iconUrl;
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "GLOBAL_MARK")
	private Long globalMark;
	@Column(name = "VER_MARK")
	private Integer verMark;
	@DateTimeFormat(iso=ISO.DATE) 
	@Column(name="CREATE_TIME",updatable=false)
	private Date createTime;	
	@DateTimeFormat(iso=ISO.DATE)
	@Column(name = "UPDATE_TIME")
	private Date updateTime;	
	public String getSnapUrl() {
		return snapUrl_default;
	}

	public void setSnapUrl(String snapUrl) {
		this.snapUrl_default = snapUrl;
	}

	public String getSnapUrl_1() {
		return snapUrl_1;
	}

	public void setSnapUrl_1(String snapUrl_1) {
		this.snapUrl_1 = snapUrl_1;
	}

	public String getSnapUrl_2() {
		return snapUrl_2;
	}

	public void setSnapUrl_2(String snapUrl_2) {
		this.snapUrl_2 = snapUrl_2;
	}

	public String getSnapUrl_3() {
		return snapUrl_3;
	}

	public void setSnapUrl_3(String snapUrl_3) {
		this.snapUrl_3 = snapUrl_3;
	}

	public String getSnapUrl_4() {
		return snapUrl_4;
	}

	public void setSnapUrl_4(String snapUrl_4) {
		this.snapUrl_4 = snapUrl_4;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVer() {
		return this.appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeSeq() {
		return this.typeSeq;
	}

	public void setTypeSeq(String typeSeq) {
		this.typeSeq = typeSeq;
	}

	public String getMinSdkVer() {
		return this.minSdkVer;
	}

	public void setMinSdkVer(String minSdkVer) {
		this.minSdkVer = minSdkVer;
	}

	public String getAppPackage() {
		return this.appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public Float getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Float fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getAuthorId() {
		return this.authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return this.authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAppWebsite() {
		return this.appWebsite;
	}

	public void setAppWebsite(String appWebsite) {
		this.appWebsite = appWebsite;
	}

	public String getSupportEmail() {
		return this.supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getKeyWords() {
		return this.keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getAppSummary() {
		return this.appSummary;
	}

	public void setAppSummary(String appSummary) {
		this.appSummary = appSummary;
	}

	public String getAppDesc() {
		return this.appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public Short getSoftLevel() {
		return this.softLevel;
	}

	public void setSoftLevel(Short softLevel) {
		this.softLevel = softLevel;
	}

	public Short getAppState() {
		return this.appState;
	}

	public void setAppState(Short appState) {
		this.appState = appState;
	}

	public Integer getDownTimes() {
		return this.downTimes;
	}

	public void setDownTimes(Integer downTimes) {
		this.downTimes = downTimes;
	}

	public Integer getCommentTimes() {
		return this.commentTimes;
	}

	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}

	public Long getIconFileId() {
		return this.iconFileId;
	}

	public void setIconFileId(Long iconFileId) {
		this.iconFileId = iconFileId;
	}

	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getGlobalMark() {
		return this.globalMark;
	}

	public void setGlobalMark(Long globalMark) {
		this.globalMark = globalMark;
	}

	public Integer getVerMark() {
		return this.verMark;
	}

	public void setVerMark(Integer verMark) {
		this.verMark = verMark;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAppState_Name() {
		return appState_Name;
	}

	public void setAppState_Name(String appState_Name) {
		this.appState_Name = appState_Name;
	}

	public void setTypeId_Name(String typeId_Name) {
		this.typeId_Name = typeId_Name;
	}

	public String getTypeId_Name() {
		return typeId_Name;
	}

	public void setDlClient(String dlClient) {
		this.dlClient = dlClient;
	}

	public String getDlClient() {
		return dlClient;
	}
	
}