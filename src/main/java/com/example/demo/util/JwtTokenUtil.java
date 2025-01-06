package com.example.demo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.config.security.JwtConstants;
import com.example.demo.domain.share.JwtTokenGenerated;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	@Value("${jwt.secret.key}")
	private String secretKeyValue;
	private static String secretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long tokenExpirationValue; // 過期時間若是3600L秒，即1個小時
	private static  long tokenExpiration;
	
	@Value("${jwt.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationValue; // 過期時間若是3600L秒，即1個小時
	private static  long refreshTokenExpiration;
	
	private static final String ISS = "SYSTEM"; // 簽發者

	/**
	 * 初始化值，由於 static 值不接受 @Value 傳入的值，在此進行初始化
	 */
	@PostConstruct
	public void init() {
		secretKey = secretKeyValue;
		tokenExpiration = tokenExpirationValue;
		refreshTokenExpiration = refreshTokenExpirationValue;
		
	}

	/**
	 * 建立 JwtToken & RefreshToken
	 * 
	 * @param username 使用者名稱
	 * @param email    使用者信箱
	 * @param roles    - 角色權限清單
	 * @param groups   - 功能清單
	 * @return JwtokenGenerated
	 */
	public static JwtTokenGenerated generateToken(String username, String email, List<String> roles, List<String> groups) {
		String token = generateToken(username, email, roles, groups, tokenExpiration);
		String refreshToken = generateToken(username, email, roles, groups, refreshTokenExpiration);
		return new JwtTokenGenerated(token, refreshToken);
	}

	/**
	 * 建立 token 參數:
	 * 
	 * @param username   使用者名稱
	 * @param email      使用者信箱
	 * @param roles      - 角色權限清單
	 * @param groups     - 功能清單
	 * @param expiration 過期時間 (秒)
	 * @return JWToken
	 */
	public static String generateToken(String username, String email, List<String> roles, List<String> groups,
			long expiration) {
		log.debug("有效時間: {}", expiration);
		HashMap<String, Object> map = new HashMap<>();
		map.put(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue(), email);
		map.put(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roles); // 個人角色
		map.put(JwtConstants.JWT_CLAIMS_KEY_GROUP.getValue(), groups); // 群組角色
		log.info("map: {}", map);

		Date issDate = new Date(System.currentTimeMillis()); // 簽發日+7天
		log.info("建立Token時的簽發日: {}", issDate);

		Date expDate = new Date(System.currentTimeMillis() + expiration * 1000); // 過期日+7天
		log.info("建立Token欲設置的過期日: {}", expDate);
		return Jwts.builder().claims(map) // 角色
				.issuer(ISS) // 簽發者
				.subject(username) // 使用者名稱
				.issuedAt(issDate) // 簽發日期
				.expiration(expDate) // 過期時間
				.signWith(getSigningKey(), Jwts.SIG.HS256) // 密鑰簽名
				.compact();
	}

	/**
	 * 從 token 中取得使用者名稱
	 * 
	 * @param token
	 * @return 使用者名稱
	 */
	public static String getUsername(String token) {
		log.info("getUsername: " + getTokenBody(token).getSubject());
		log.info("TokenBody: " + getTokenBody(token));
		return getTokenBody(token).getSubject();
	}

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	public static List<String> getRoleList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
	}

	/**
	 * 從 token 中取得使用者信箱
	 * 
	 * @param token
	 * @return 使用者信箱
	 */
	public static String getEmail(String token) {
		return (String) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue());
	}

	/**
	 * 取得簽發日
	 * 
	 * @param token
	 * @return 簽發日
	 */
	public static Date getIssAt(String token) {
		return getTokenBody(token).getIssuedAt();
	}

	/**
	 * 取得過期日
	 * 
	 * @param token
	 * @return 過期日
	 */
	public static Date getExpDate(String token) {
		return getTokenBody(token).getExpiration();
	}

	/**
	 * 是否已過期
	 * 
	 * @param token
	 * @return true/false
	 */
	public static boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
	}

	/**
	 * 取得 Token 主體
	 * 
	 * @param token
	 * @return Claims
	 */
	public static Claims getTokenBody(String token) {
		// 使用 Jwts.parser() 建立 JwtParser 實例
		return Jwts.parser().verifyWith(getSigningKey()) // 用於設定金鑰，該金鑰用於驗證 JWT 令牌的簽名
				.build() // 建置 JwtParser 實例
				.parseSignedClaims(token).getPayload();
	}

	/**
	 * 解析 Token
	 * 
	 * @param token
	 * @return 解析後的結果 Map
	 */
	public static Map<String, Object> parseToken(String token) {
		Claims claims = getTokenBody(token);
		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 取得 SigningKey
	 * 
	 * @return key
	 */
	private static SecretKey getSigningKey() {
		log.info("Secret Key: {}", secretKey);
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
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
