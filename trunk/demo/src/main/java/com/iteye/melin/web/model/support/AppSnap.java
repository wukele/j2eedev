package com.iteye.melin.web.model.support;

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
@Table(name = "app_j_screenshot")
public class AppSnap {

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "SCREENSHOT_ID")
	private Long id;
	@Column(name = "APP_ID")
	private Long appId;
	@Column(name = "FILE_ID")
	private Long  fileId;
	@Column(name = "SCREENSHOT_URL")
	private String snapUrl;


	public Long getId() {
		return this.id;
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

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getSnapUrl() {
		return snapUrl;
	}

	public void setSnapUrl(String snapUrl) {
		this.snapUrl = snapUrl;
	}

}