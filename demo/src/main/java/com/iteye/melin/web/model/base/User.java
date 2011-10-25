package com.iteye.melin.web.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="usr_e_user", schema="demo")
public class User {
	@Id
	@Column(name="USER_ID")
    @GeneratedValue(generator = "tableGenerator")     
	@GenericGenerator(name = "tableGenerator", strategy="increment")
	private Long id;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="DEL_FLAG")
	private String delFlag;
	@Transient
	private String delFlag_Name;
	
	@Column(name="PWD_UPD_LST_TME")
	private Date pwdUpdLstTme;
	
	@Column(name="E_MAIL")
	private String email;
	
	@Column(name="ZIP_CODE")
	private String zipCode;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="FAX_NO")
	private String faxNo;
	
	@Column(name="LINK_TEL1")
	private String linkTel1;
	
	@Column(name="LINK_TEL2")
	private String linkTel2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDelFlag_Name() {
		return delFlag_Name;
	}

	public void setDelFlag_Name(String delFlag_Name) {
		this.delFlag_Name = delFlag_Name;
	}

	public Date getPwdUpdLstTme() {
		return pwdUpdLstTme;
	}

	public void setPwdUpdLstTme(Date pwdUpdLstTme) {
		this.pwdUpdLstTme = pwdUpdLstTme;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getLinkTel1() {
		return linkTel1;
	}

	public void setLinkTel1(String linkTel1) {
		this.linkTel1 = linkTel1;
	}

	public String getLinkTel2() {
		return linkTel2;
	}

	public void setLinkTel2(String linkTel2) {
		this.linkTel2 = linkTel2;
	}
}
