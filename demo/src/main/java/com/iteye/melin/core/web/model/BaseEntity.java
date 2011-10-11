package com.iteye.melin.core.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @datetime 2010-8-12 下午05:15:55
 * @author libinsong1204@gmail.com
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//~ Instance fields ================================================================================================
	@Id
	@Column(name="RES_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="com.sumilux.doce.core.key.SequenceGenerator")
    private Long id;
	
	@Column(name="RES_CREATOR", updatable=false)
	private String creator;
	@Column(name="RES_EDITOR", insertable=false)
	private String editor;
	
	@Temporal(TemporalType.TIMESTAMP)//不用set,hibernate会自动把当前时间写入  
    @Column(name="RES_CREATE_DATE", updatable = false)  
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)//不用set,hibernate会自动把当前时间写入  
	@Column(name="RES_EDIT_DATE", insertable = false)  
	private Date editDate;
	
	//~ Methods ========================================================================================================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
}
