package com.example.demo.domain.group.aggregate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.command.CreateGroupCommand;
import com.example.demo.domain.group.command.CreateOrUpdateGroupCommand;
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
 * 群組表
 */
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_info")
@EntityListeners(AuditingEntityListener.class)
public class GroupInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type; // 群組種類

	private String name; // 名稱

	private String code; // 群組代號

	// 使用懶加載，避免 N+1 query 效能問題
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "group_id")
	private List<GroupRole> roles = new ArrayList<>(); // 角色所屬群組

	private String description; // 敘述

	@Enumerated(EnumType.STRING)
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 建立一筆群組資料
	 * 
	 * @param command
	 */
	public void create(CreateGroupCommand command) {
		this.type = command.getType();
		this.name = command.getName();
		this.code = command.getCode();
		this.description = command.getDescription();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 建立一筆群組資料
	 * 
	 * @param command
	 */
	public void create(CreateOrUpdateGroupCommand command) {
		this.type = command.getType();
		this.name = command.getName();
		this.code = command.getCode();
		this.description = command.getDescription();
		this.activeFlag = YesNo.Y;
	}

	/**
	 * 更新一筆群組資料
	 * 
	 * @param command
	 */
	public void update(CreateOrUpdateGroupCommand command) {
		this.name = command.getName();
		this.type = command.getType();
		this.code = command.getCode();
		this.description = command.getDescription();
		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
	}

	/**
	 * 更新群組角色
	 * 
	 * @param roleIds 欲變更的群組角色 ID
	 */
	public void updateRoles(List<GroupRole> groupRoles) {
		
		// 移除 functions 中不存在於 Role Functions 的項目
		this.roles.removeIf(
				existingFunction -> groupRoles.stream().noneMatch(newRole -> newRole.equals(existingFunction)));
		// 增加新的 Role Function
		groupRoles.stream().forEach(newRole -> {
			if (!this.roles.contains(newRole)) {
				this.roles.add(newRole);
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
