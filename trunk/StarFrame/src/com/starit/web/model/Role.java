package com.starit.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * Spring Security权限的扩展，对于角色需要实现GrantedAuthority接口
 *
 * @datetime 2010-8-8 下午08:01:43
 * @author libinsong1204@gmail.com
 */
@Entity
@Table(name="ST_ROLE")
public class Role implements GrantedAuthority, Serializable {
    //~ Instance fields ================================================================================================
    private static final long serialVersionUID = 1L;
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String creator;
	private String updator;
	private Date createTime;
	private Date updateTime;
	
    @ManyToMany(mappedBy="authorities", fetch=FetchType.LAZY)
    private Set<User> users = new HashSet<User>();
    
    @ManyToMany(targetEntity=Role.class,fetch=FetchType.LAZY)
    @JoinTable(name="ST_ROLE_RESOURCE",
            joinColumns={@JoinColumn(name="ROLE_ID")},
            inverseJoinColumns={@JoinColumn(name="RESOURCE_ID")}
    )
    private Set<Resource> resources = new HashSet<Resource>();

    //~ Constructors ===================================================================================================
    public Role() {
    }
    
    public Role(long id, String code, String name) {
        Assert.hasText(code, "A granted authority textual representation is required");
        this.id = id;
        this.code = code;
        this.name = name;
    }

    //~ Methods ========================================================================================================
    public String getAuthority() {
		return this.code;
	}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	

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

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public int hashCode() {
        return this.code.hashCode();
    }

	public String toString() {
        return this.code + "," + this.name;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.code);
        }

        if (obj instanceof GrantedAuthority) {
            GrantedAuthority attr = (GrantedAuthority) obj;

            return this.code.equals(attr.getAuthority());
        }

        return false;
    }
}