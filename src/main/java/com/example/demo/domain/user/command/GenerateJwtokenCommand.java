package com.example.demo.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateJwtokenCommand {

	private String username;
	
	private String password;
	
	
}
