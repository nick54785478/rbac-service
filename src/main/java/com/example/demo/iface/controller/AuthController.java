package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.AuthQueryService;
import com.example.demo.iface.dto.in.MaintainPermissionQueriedResource;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthQueryService authQueryService;

	/**
	 * 查詢維護頁面權限清單 (決定左側邊框能看到什麼)
	 * 
	 * @param username 使用者名稱
	 * @return ResponseEntity<MaintainPermissionQueriedResource>
	 */
	@GetMapping("/permissions")
	public ResponseEntity<MaintainPermissionQueriedResource> getMaintainPermissions(@RequestParam(required = false) String username) {
		List<String> permissions = authQueryService.getMaintainPermissions(username);
		return new ResponseEntity<>(new MaintainPermissionQueriedResource(permissions), HttpStatus.OK);
	}

}
