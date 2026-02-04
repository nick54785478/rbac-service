package com.example.demo.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDetailsQueried {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;
	
	private String birthday;
	
	private String nationalIdNo;
	
	private String activeFlag;

	private List<UserGroupDetailsQueried> groups;

	private List<UserRoleDetailsQueried> roles;
	
	private List<FunctionInfoDetailsQueried> functions;

}
