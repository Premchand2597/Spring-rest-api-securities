package com.example.SpringAi_Demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_table")
public class Dept_Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private long roleId;
	@Column(name = "role_name")
	private String roleName; 
	private String description;
	@Column(name = "dept_id")
	private int deptId;
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	@Override
	public String toString() {
		return "Dept_Entity [roleId=" + roleId + ", roleName=" + roleName + ", description=" + description + ", deptId="
				+ deptId + "]";
	}
}
