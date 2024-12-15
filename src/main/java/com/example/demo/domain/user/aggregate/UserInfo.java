package com.example.demo.domain.user.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.util.PasswordUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色表
 */
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name; // 使用者姓名

	private String email; // 信箱

	private String username; // 帳號

	private String password; // 密碼
	
	@Column(name = "national_id")
	private String nationalIdNo; // 身分證字號
	
	private Date birthday; // 出生年月日

	private String address;

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<UserRole> roles = new ArrayList<>(); // 角色清單

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY,  orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<UserGroup> groups = new ArrayList<>(); // 角色所屬群組

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * Constructor
	 */
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 新增使用者資料
	 * 
	 * @param command
	 * @param passwordEncoder 密碼加密器
	 */
	public void create(CreateUserCommand command) {
		this.name = command.getName();
		this.username = command.getUsername();
		this.password = PasswordUtil.encode(command.getPassword());
		this.birthday = command.getBirthday();
		this.nationalIdNo = command.getNationalId();
		this.email = command.getEmail();
		this.address = command.getAddress();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 */
	public void update(UpdateUserCommand command) {
		this.birthday = command.getBirthday();
		this.nationalIdNo = command.getNationalId();
		this.name = (StringUtils.isNotBlank(command.getName())) ? command.getName() : this.name;
		this.email = (StringUtils.isNotBlank(command.getEmail())) ? command.getEmail() : this.email;
		this.address = (StringUtils.isNotBlank(command.getAddress())) ? command.getAddress() : this.address;
	}

	/**
	 * 更新 角色清單
	 * 
	 * @param userRoles
	 */
	public void updateRoles(List<UserRole> userRoles) {
		// 移除 functions 中不存在於 Role Functions 的項目
		this.roles.removeIf(existingRole -> userRoles.stream().noneMatch(newRole -> newRole.equals(existingRole)));
		// 增加新的 Role Function
		userRoles.stream().forEach(newRole -> {
			if (!roles.contains(newRole)) {
				this.roles.add(newRole);
			}
		});
	}


	/**
	 * 更新使用者群組
	 * 
	 * @param userGroups 使用者群組清單
	 */
	public void updateGroups(List<UserGroup> userGroups) {
		// 移除 functions 中不存在於 Role Functions 的項目
		this.groups.removeIf(
				existingGroup -> userGroups.stream().noneMatch(newGroup -> newGroup.equals(existingGroup)));
		// 增加新的使用者群組
		userGroups.stream().forEach(newGroup -> {
			if (!this.groups.contains(newGroup)) {
				this.groups.add(newGroup);
			}
		});
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 * 
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
