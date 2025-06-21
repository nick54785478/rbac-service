package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupQueriedResource {

	private Long id;

	private String service;

	private String type;

	private String name; // 名稱

	private String code; // 群組代號

	private String description; // 敘述

	private String activeFlag;
}
