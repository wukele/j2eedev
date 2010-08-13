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
	//~ Instance fields ================================================================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type;
	private int priority;
	private String action;
	private String descn;
	private boolean enabled;
	
	@ManyToMany(mappedBy="resources", fetch=FetchType.LAZY)
	private Set<Role> roles = new HashSet<Role>();

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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@JsonIgnore
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public static enum ResourceType {
		METHOD("method"), URL("url");
		
		private ResourceType(String type){   
           this.type = type;
        }   
		
		private String type;
		public String toString(){
            return type;   
		}   
	}
	
	//测试
	public static void main(String[] args) {
		ResourceType resourceType = ResourceType.METHOD;
		System.out.println(resourceType);
	}
}
