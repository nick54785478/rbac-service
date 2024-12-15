package com.example.demo.domain.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdated {

	private Long id;

	private String name;
	
	private String username; // 帳號

	private String email;
	
	private String birthday;
	
	private String nationalId;

	private String address;
	
}
