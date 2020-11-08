package com.dity.common.bootonfig;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenService {

	public String getToken(Map<String,Object> user) {
		Date start = new Date();
		long currentTime = System.currentTimeMillis() + 60 * 60 * 1000;//一小时有效时间
		Date end = new Date(currentTime);
		String token = "";
		
		token = JWT.create().withAudience((String)user.get("USER_ID")).withIssuedAt(start).withExpiresAt(end)
				.sign(Algorithm.HMAC256((String)user.get("PASS")));
		return token;
	}
}
