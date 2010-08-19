package com.starit.core.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @datetime 2010-8-12 下午05:15:55
 * @author libinsong1204@gmail.com
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//~ Instance fields ================================================================================================
	private String creator;
	private String updator;
	
	@Temporal(TemporalType.TIMESTAMP)//不用set,hibernate会自动把当前时间写入  
    @Column(updatable = false, length = 20)  
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)//不用set,hibernate会自动把当前时间写入  
	private Date updateTime;
	
	//~ Methods ========================================================================================================
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
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
}
