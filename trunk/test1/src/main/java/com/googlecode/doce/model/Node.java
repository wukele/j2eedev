package com.googlecode.doce.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name="DOCE_NODE")
public class Node implements java.io.Serializable {
	@Id
	@GeneratedValue(generator = "tableGenerator")     
    @GenericGenerator(name = "tableGenerator", strategy="assigned")
	@Column(name = "NODE_ID")
	private String nodeId;
	@Column(name = "NODE_NAME")
	private String nodeName;
	@Column(name = "PARENT_DIR_ID")
	private String parentDirId;
	@Column(name = "NODE_TYPE")
	private int nodeType;
	@Column(name = "MODIFIED")
	private Date modified;
	@Column(name = "OWNER")
	private String owner;
	@Column(name = "CURRENT_STATUS")
	private int currentStatus;
	@Column(name = "SHARED_NODE_ID")
	private String sharedNodeId;
	@Column(name = "SHARER")
	private String sharer;
	
	//file properties
	@Column(name = "FILE_SIZE")
	private Long fileSize;
	@Column(name = "FILE_CODE")
	private String fileCode;
	@Column(name = "FILE_PAGES")
	private int filePages;
	@Column(name = "MIME_TYPE")
	private String mimeType;

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParentDirId() {
		return this.parentDirId;
	}

	public void setParentDirId(String parentDirId) {
		this.parentDirId = parentDirId;
	}

	public String getSharedNodeId() {
		return sharedNodeId;
	}

	public void setSharedNodeId(String sharedNodeId) {
		this.sharedNodeId = sharedNodeId;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public int getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public Date getModified() {
		return this.modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFileCode() {
		return this.fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public int getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getFilePages() {
		return filePages;
	}

	public void setFilePages(int filePages) {
		this.filePages = filePages;
	}

	public String getId() {
		return nodeId;
	}
}