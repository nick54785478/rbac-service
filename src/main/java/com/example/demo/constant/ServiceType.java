package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ServiceType {

	AUTH_SERVICE("AUTH_SERVICE"), TRAIN_SERVICE("TRAIN_SERVICE");
	
	
	@Getter
	private final String value;
}
