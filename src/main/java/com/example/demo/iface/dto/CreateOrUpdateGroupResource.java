package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateGroupResource {

	private Long id;
	
	private String service;
	
	private String type;
	
	private String code;
	
	private String name;
	
	private String description;
	
	private String activeFlag;
	
}
