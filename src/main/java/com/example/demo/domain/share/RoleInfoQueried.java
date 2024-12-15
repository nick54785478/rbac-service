package com.example.demo.domain.share;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.share.enums.YesNo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoQueried {

	private Long id;

	private String code; // 角色 Code

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private List<RoleFunctionQueried> functions = new ArrayList<>(); // 角色所屬功能

	private YesNo activeFlag = YesNo.Y; // 是否有效
}
