package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.config.security.JwtConstants;
import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.JwtTokenGenerated;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserRoleQueried;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.command.GenerateJwtokenCommand;
import com.example.demo.domain.user.command.RefreshTokenCommand;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.JwtTokenUtil;
import com.example.demo.util.PasswordUtil;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用於執行 Jwt 相關操作的 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenCommandService {

	private final UserService userService;
	private final UserInfoRepository userInfoRepository;

	/**
	 * 建立 JWToken
	 * 
	 * @param command
	 * @return token
	 */
	@Transactional
	public JwtTokenGenerated generateToken(GenerateJwtokenCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());

		if (Objects.isNull(userInfo)) {
			throw new ValidationException("VALIDATION_FAILED", "該使用者帳號不存在");// 比對失敗
		}

		boolean checkPassword = PasswordUtil.checkPassword(command.getPassword(), userInfo.getPassword());
		log.info("command ", command.getUsername(), command.getPassword());

		// 檢查密碼是否相符
		if (!checkPassword) {
			throw new ValidationException("VALIDATION_FAILED", "使用者帳號或密碼有誤");// 比對失敗
		}

		// 查詢該使用者所在的群組
		List<UserGroupQueried> queryGroups = userService.queryGroups(command.getUsername());
		List<String> groups = queryGroups.stream().map(UserGroupQueried::getCode).collect(Collectors.toList());

		// 查詢該使用者個人角色
		List<UserRoleQueried> queryRoles = userService.queryRoles(command.getUsername());
		List<String> roles = queryRoles.stream().map(UserRoleQueried::getCode).collect(Collectors.toList());
		JwtTokenGenerated resource = JwtTokenUtil.generateToken(userInfo.getUsername(), userInfo.getEmail(), roles,
				groups);

		// 若不存在 RefreshToken，設置進去
		if (StringUtils.isBlank(userInfo.getRefreshToken())) {
			userInfo.updateRefreshToken(resource.getRefreshToken());
		} else {
			// 過期日
			Date expDate = JwtTokenUtil.getExpDate(resource.getRefreshToken());
			// 現在時間比過期日大
			if (new Date().after(expDate)) {
				userInfo.updateRefreshToken(resource.getRefreshToken());
			}
		}
		userInfoRepository.save(userInfo);
		return resource;
	}

	/**
	 * 刷新 Token
	 * 
	 * @param command
	 * @return JwtokenGenerated
	 */
	public JwtTokenGenerated refresh(RefreshTokenCommand command) {
		UserInfo userInfo = userInfoRepository.findByRefreshToken(command.getToken());
		if (!Objects.isNull(userInfo)) {
			// 查詢該使用者個人角色
			List<UserGroupQueried> queryGroups = userService.queryGroups(userInfo.getUsername());
			List<String> groups = queryGroups.stream().map(UserGroupQueried::getCode).collect(Collectors.toList());
			List<UserRoleQueried> queryRoles = userService.queryRoles(userInfo.getUsername());
			List<String> roles = queryRoles.stream().map(UserRoleQueried::getCode).collect(Collectors.toList());
			JwtTokenGenerated tokenGenerated = JwtTokenUtil.generateToken(userInfo.getUsername(), userInfo.getEmail(),
					roles, groups);
			// 更新 Refresh Token
			userInfo.updateRefreshToken(tokenGenerated.getRefreshToken());
			userInfoRepository.save(userInfo);
			return tokenGenerated;

		}
		return null;
	}

	/**
	 * 從 token 中取得使用者名稱
	 * 
	 * @param token
	 * @return 使用者名稱
	 */
	public String getUsername(String token) {
		log.info("getUsername: {}", getTokenBody(token).getSubject());
		log.info("TokenBody: {}", getTokenBody(token));
		return getTokenBody(token).getSubject();
	}

	/**
	 * 取得使用者信箱
	 * 
	 * @param token
	 * @return 使用者信箱
	 */
	public String getEmail(String token) {
		return (String) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue());
	}

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	public List<String> getRoleList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
	}

	/**
	 * 取得簽發日
	 * 
	 * @param token
	 * @return 簽發日
	 */
	public Date getIssAt(String token) {
		return getTokenBody(token).getIssuedAt();
	}

	/**
	 * 取得過期日
	 * 
	 * @param token
	 * @return 過期日
	 */
	public Date getExpDate(String token) {
		return getTokenBody(token).getExpiration();
	}

	/**
	 * 是否已過期
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
	}

	/**
	 * 取得 Token 主體
	 * 
	 * @param token
	 * @return Claims
	 */
	public Claims getTokenBody(String token) {
		return JwtTokenUtil.getTokenBody(token);
	}

	/**
	 * 解析 Token
	 * 
	 * @param token
	 * @return 解析後的結果 Map
	 */
	public Map<String, Object> parseToken(String token) {
		Claims claims = getTokenBody(token);
		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 驗證 JWToken 合法性
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean validateToken(String token) {
		Claims claims = this.getTokenBody(token);
		return claims != null;
	}

}
