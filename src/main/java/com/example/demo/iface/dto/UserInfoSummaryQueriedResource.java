package com.example.demo.iface.dto;

import java.util.List;

import com.example.demo.domain.share.FunctionInfoSummaryQueriedResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoSummaryQueriedResource {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String address;
	
	private String birthday;
	
	private String nationalIdNo;

	private List<UserGroupSummaryQueriedResource> groups;

	private List<UserRoleSummaryQueriedResource> roles;
	
	private List<FunctionInfoSummaryQueriedResource> functions;

}
