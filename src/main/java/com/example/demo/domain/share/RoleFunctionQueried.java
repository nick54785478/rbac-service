package com.example.demo.domain.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunctionQueried {

	private Long id;
	
	private String service;

	private String type;

	private String code;

	private String name;

	private String actionType;

	private String description;

	private String activeFlag;

}
