package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFunctionResource {

	private String type; // 種類
	
	private String name;
	
	private String actionType;
	
	private String code; // Code

	private String description; // 敘述
}
