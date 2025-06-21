package com.example.demo.infra.assembler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.RoleFunctionQueried;
import com.example.demo.domain.share.RoleInfoQueried;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RoleAssembler {

	/**
	 * 組裝 RoleInfoQueried 資料
	 * 
	 * @param role
	 * @param functions
	 * @return RoleInfoQueried
	 */
	public RoleInfoQueried assembleRoleQueried(RoleInfo role, List<FunctionInfo> functions) {
		List<RoleFunctionQueried> functionRoles = BaseDataTransformer.transformData(functions,
				RoleFunctionQueried.class);
		RoleInfoQueried roleQueried = BaseDataTransformer.transformData(role, RoleInfoQueried.class);
		roleQueried.setFunctions(functionRoles);
		return roleQueried;
	}
}
