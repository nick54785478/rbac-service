package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateFunctionResource {

	private Long id;
	
	private String service; // 服務配置

	private String code;
	
	private String name;

	private String type;
	
	private String actionType;
	
	private String description;
	
	private String activeFlag;
	
}
