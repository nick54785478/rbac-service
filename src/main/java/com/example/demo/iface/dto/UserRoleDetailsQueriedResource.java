package com.example.demo.iface.dto;

import com.example.demo.domain.share.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDetailsQueriedResource {

	private Long id;
	
	private String service;
	
	private String code;

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private YesNo activeFlag;
}