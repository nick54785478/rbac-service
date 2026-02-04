package com.example.demo.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRolesQueried {

	private Long groupId;
	
	private List<GroupRoleQueried> roleList;
}
