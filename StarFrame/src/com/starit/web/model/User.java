package com.starit.web.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="ST_USER")
public class User implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long operatorId;
	private String userId;     	
	private String password;      	
	private Date passwdInvalDate;
	private Long orgId;
	@Transient
	private String orgName;
	private String operatorName;  	 		
	private Date unlockTime;	
	private Date lastLogin;   		
	private int errCount;   			
	private String macCode;     	
	private String ipAddress;	
	private String email;
	@ManyToMany(targetEntity=Role.class,fetch=FetchType.LAZY)
    @JoinTable(name="ST_USER_ROLE",
            joinColumns={@JoinColumn(name="OPERATOR_ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID")}
    )
    private Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Employee employee;
	
	@JsonProperty("id")
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getPasswdInvalDate() {
		return passwdInvalDate;
	}
	public void setPasswdInvalDate(Date passwdInvalDate) {
		this.passwdInvalDate = passwdInvalDate;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Date getUnlockTime() {
		return unlockTime;
	}
	public void setUnlockTime(Date unlockTime) {
		this.unlockTime = unlockTime;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public int getErrCount() {
		return errCount;
	}
	public void setErrCount(int errCount) {
		this.errCount = errCount;
	}
	public String getMacCode() {
		return macCode;
	}
	public void setMacCode(String macCode) {
		this.macCode = macCode;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@JsonIgnore
	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	//------------------UserDetails接口方法--------------------------------------
	public String getUsername() {
		return this.userId;
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public boolean isEnabled() {
		return true;
	}
}
