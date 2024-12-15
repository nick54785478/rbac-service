package com.example.demo.domain.role.aggregate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.share.enums.YesNo;

import jakarta.persistence.CascadeType;
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
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_info")
@EntityListeners(AuditingEntityListener.class)
public class RoleInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "role_id")
	private List<RoleFunction> functions = new ArrayList<>(); // 角色所屬功能

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 新增一筆角色資料
	 * 
	 * @param command
	 */
	public void create(CreateRoleCommand command) {
		this.code = command.getCode();
		this.name = command.getName();
		this.description = command.getDescription();
		this.type = command.getType();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 新增一筆角色資料
	 * 
	 * @param command
	 */
	public void create(CreateOrUpdateRoleCommand command) {
		this.code = command.getCode();
		this.name = command.getName();
		this.description = command.getDescription();
		this.type = command.getType();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新一筆角色資料
	 * 
	 * @param command
	 */
	public void update(UpdateRoleCommand command) {
		this.code = command.getCode();
		this.name = command.getName();
		this.type = command.getType();
		this.description = command.getDescription();
	}
	
	/**
	 * 更新一筆角色資料
	 * 
	 * @param command
	 */
	public void update(CreateOrUpdateRoleCommand command) {
		this.id = command.getId();
		this.code = command.getCode();
		this.name = command.getName();
		this.type = command.getType();
		this.description = command.getDescription();
		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
	}

	/**
	 * 更新角色 Function 清單，使其有權限執行相關動作
	 * 
	 * @param roleFunctions
	 */
	public void updateFunctions(List<RoleFunction> roleFunctions) {
		// 移除 functions 中不存在於 Role Functions 的項目
		this.functions.removeIf(
				existingFunction -> roleFunctions.stream().noneMatch(newFunc -> newFunc.equals(existingFunction)));
		// 增加新的 Role Function
		roleFunctions.stream().forEach(newFunction -> {
			if (!functions.contains(newFunction)) {
				functions.add(newFunction);
			}
		});
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 */
	public void delete() {
		this.activeFlag = YesNo.N;
	}

}
