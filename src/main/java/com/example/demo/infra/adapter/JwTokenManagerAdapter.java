package com.example.demo.infra.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.domain.dto.JwtTokenGenerated;
import com.example.demo.infra.jwt.JwtTokenParser;
import com.example.demo.infra.jwt.JwtTokenGenerator;
import com.example.demo.infra.jwt.JwtTokenValidator;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class JwTokenManagerAdapter implements JwTokenManagerPort {

	private JwtTokenGenerator jwTokenGenerator;
	private JwtTokenParser jwTokenDecorder;
	private JwtTokenValidator jwTokenValidator;

	@Override
	public JwtTokenGenerated generateToken(String username, String email, List<String> roles, List<String> groups) {
		return jwTokenGenerator.generateToken(username, email, roles, groups);
	}

	@Override
	public boolean validateToken(String token) {
		return jwTokenValidator.validateToken(token);
	}

	@Override
	public String getEmail(String token) {
		return jwTokenDecorder.getEmail(token);
	}

	@Override
	public List<String> getRoleList(String token) {
		return jwTokenDecorder.getRoleList(token);
	}

	@Override
	public String getUsername(String token) {
		return jwTokenDecorder.getUsername(token);
	}

	@Override
	public boolean isExpiration(String token) {
		return jwTokenDecorder.getTokenBody(token).getExpiration().before(new Date());
	}

}
