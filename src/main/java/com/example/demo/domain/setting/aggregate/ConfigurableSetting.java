package com.example.demo.domain.setting.aggregate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.domain.setting.command.CreateSettingCommand;
import com.example.demo.domain.setting.command.UpdateSettingCommand;
import com.example.demo.domain.share.enums.YesNo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "setting")
@EntityListeners(AuditingEntityListener.class)
public class ConfigurableSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "data_type")
	private String dataType;	// 資料類型
	
	private String type; // 種類

	private String name; // 名稱

	private String code; // 代碼
	
	private String value; // 值

	private String description; // 敘述

	@Column(name = "priority_no")
	private Integer priorityNo; // 順序號(從 1 開始)

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private YesNo activeFlag = YesNo.Y; // 是否有效

	/**
	 * 建立一筆 Setting
	 * 
	 * @param command
	 */
	public void create(CreateSettingCommand command) {
		this.dataType = command.getDataType();
		this.type = command.getType();
		this.name = command.getName();
		this.value = command.getValue();
		this.code = command.getCode();
		this.description = command.getDescription();
		this.priorityNo = command.getPriorityNo();
		this.activeFlag = YesNo.Y;
	}
	
	/**
	 * 修改一筆 Setting
	 * 
	 * @param command
	 */
	public void update(UpdateSettingCommand command) {
		this.dataType = command.getDataType();
		this.type = command.getType();
		this.name = command.getName();
		this.value = command.getValue();
		this.code = command.getCode();
		this.description = command.getDescription();
		this.priorityNo = command.getPriorityNo();
		this.activeFlag = YesNo.valueOf(command.getActiveFlag());
	}
	
	/**
	 * 刪除 (更改 activeFlag = 'N')
	 * */
	public void delete() {
		this.activeFlag = YesNo.N;
	}
}
