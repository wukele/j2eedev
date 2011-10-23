package com.iteye.melin.web.model.support;

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
@Table(name = "app_e_file")
public class AppFile {
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "FILE_ID")
	private Long id;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "ABSOLUTEPATH")
	private String filepath;
	@Column(name = "FILE_SIZE")
	private Integer filesize;
	@Column(name = "FILE_TYPE")
	private String filetype;
	@Column(name = "CREATE_TIME")
	private Date createTime;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public Date getCreateTime() {
		return createTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Integer getFilesize() {
		return filesize;
	}

	public void setFilesize(Integer filesize) {
		this.filesize = filesize;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}