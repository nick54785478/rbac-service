package com.example.demo.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesQueried {

	private String username;

	private List<UserRoleQueried> userRoleList;
}
