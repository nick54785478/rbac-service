package com.example.demo.domain.group.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupRolesCommand {

	private Long groupId;
	
	private String service;
	
	private List<Long> roleIds;
}
