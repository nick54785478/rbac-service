package com.example.demo.domain.shared.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunctionQueriedSummary {

	private Long id;

	private String type;

	private String code;

	private String name;

	private String actionType;

	private String description;

	private String activeFlag;

}
