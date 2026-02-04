package com.example.demo.domain.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoQueried {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;	
	
	private String nationalIdNo; // 身分證字號
	
	private Date birthday; // 出生年月日

	private List<UserGroupQueried> groups;

	private List<UserRoleQueried> roles;
	

}
