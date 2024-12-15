package com.example.demo.domain.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreated {

	private Long id;

	private String name; // 名稱
	
	private String code; // 群組代號
	
	private String description; // 敘述
}
