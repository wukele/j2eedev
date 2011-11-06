package com.iteye.melin.web.model.support;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * AppEApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "APP_J_RECO_APP")
public class AppRecoRs {

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "RECO_APP_ID")
	private Long id;
	@Column(name = "APP_ID")
	private Long appId;
	@Column(name = "RECOMMEND_ID")
	private Long  recoId;
	@Column(name = "GLOBAL_MARK")
	private Long globalMark;
	@Column(name = "ORDER_NUM")
	private String orderNum;
	@DateTimeFormat(iso=ISO.DATE) 
	@Column(name="CREATE_TIME",updatable=false)
	private Date createTime;	
	@DateTimeFormat(iso=ISO.DATE)
	@Column(name = "UPDATE_TIME")
	private Date updateTime;	


	public Long getRecoId() {
		return recoId;
	}

	public void setRecoId(Long recoId) {
		this.recoId = recoId;
	}

	public Long getGlobalMark() {
		return globalMark;
	}

	public void setGlobalMark(Long globalMark) {
		this.globalMark = globalMark;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
}