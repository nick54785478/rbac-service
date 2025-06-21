package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

	PERSONALITY("PERSONALITY", "個人角色"), GROUP("GROUP", "群組角色");

	private String name;

	private String value;

	/**
	 * 透過 Name 找到 Value
	 * 
	 * @param name
	 * @return value
	 */
	public static String getValueByName(String name) {
		for (RoleType type : RoleType.values()) {
			if (type.getName().equals(name)) {
				return type.getValue();
			}
		}
		return null; // 或丟例外 throw new IllegalArgumentException("Invalid name: " + name);
	}

}
