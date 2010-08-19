package com.starit.web.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @datetime 2010-8-18 上午11:54:41
 * @author libinsong1204@gmail.com
 */
@Entity
@Table(name="ST_OPERLOG")
public class OperLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long operatorId;
	private String operatorName;
	private String entityType;
	private String exeOperation;
	private String exeType;
	private Date createtime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getExeOperation() {
		return exeOperation;
	}
	public void setExeOperation(String exeOperation) {
		this.exeOperation = exeOperation;
	}
	public String getExeType() {
		return exeType;
	}
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}
