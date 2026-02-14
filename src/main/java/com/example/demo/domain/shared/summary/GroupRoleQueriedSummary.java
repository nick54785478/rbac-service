package com.example.demo.domain.shared.summary;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoleQueriedSummary {

	private Long id; // 角色 ID

	private String service;

	private String code; // 角色代號

	private String name; // 名稱

	@Default
	private List<RoleInfoQueriedSummary> roles = new ArrayList<>();

	private String description; // 敘述
}
