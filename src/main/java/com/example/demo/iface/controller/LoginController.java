package com.example.demo.iface.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.user.command.GenerateJwtokenCommand;
import com.example.demo.iface.dto.GenerateJwtokenResource;
import com.example.demo.iface.dto.JwtokenGeneratedResource;
import com.example.demo.service.JwtTokenCommandService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

	private JwtTokenCommandService jwtokenService;

	@PostMapping("/login")
	public ResponseEntity<JwtokenGeneratedResource> login(@RequestBody GenerateJwtokenResource resource) {
		GenerateJwtokenCommand command = BaseDataTransformer.transformData(resource, GenerateJwtokenCommand.class);
		return new ResponseEntity<>(BaseDataTransformer.transformData(jwtokenService.generateToken(command),
				JwtokenGeneratedResource.class), HttpStatus.OK);
	}
}
