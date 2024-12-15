package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreatedResource {

	private Long id;

	private String name; // 名稱
	
	private String code; // 群組代號
	
	private String description; // 敘述
}
