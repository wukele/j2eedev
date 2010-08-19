package com.starit.web.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.starit.core.web.model.BaseEntity;

/**
 *
 * @datetime 2010-8-9 下午05:23:33
 * @author libinsong1204@gmail.com
 */
@Entity
@Table(name="ST_RESOURCE")
public class Resource extends BaseEntity {
	private static final long serialVersionUID = 1L;
	//~ Instance fields ================================================================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type;
	private int priority;
	private String action;
	private String descn;
	private char enabled;
	
	@ManyToMany(mappedBy="resources", fetch=FetchType.LAZY)
	private Set<Role> roles = new HashSet<Role>();
	
	//资源关联指定角色的个数，用于判断角色是否与资源关联
	@Transient
	private Long counter = 0L;

	//~ Methods ========================================================================================================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public char getEnabled() {
		return enabled;
	}
	public void setEnabled(char enabled) {
		this.enabled = enabled;
	}
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
	}
	@JsonIgnore
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static enum ResourceType {
		METHOD("method"), URL("url"), MENU("menu");
		
		private ResourceType(String type){   
           this.type = type;
        }   
		
		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String toString(){
            return type;   
		}   
	}
}
