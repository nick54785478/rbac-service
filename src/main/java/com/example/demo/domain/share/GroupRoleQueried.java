package com.example.demo.domain.share;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoleQueried {

	private Long id; // 角色 ID

	private String code; // 角色代號

	private String name; // 名稱

	private List<RoleInfoQueried> roles;

	private String description; // 敘述
}
