package com.example.demo.domain.shared.summary;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRolesQueriedSummary {

	private Long groupId;
	
	private List<GroupRoleQueriedSummary> roleList;
}
