package com.example.demo.domain.share;

import com.example.demo.domain.share.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupQueried {

	private Long id;
	
	private String service;
	
	private String type;

	private String name; // 名稱

	private String code; // 群組代號

	private String description; // 敘述

	private YesNo activeFlag;
}
