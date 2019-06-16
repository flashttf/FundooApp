package com.bridgelabz.fundoo.utility;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;



@Component
public class TokenUtility {
	private static String secretPin="flash908642018";
	
	public static String generateToken(String id) {
		Algorithm algorithm=Algorithm.HMAC256(secretPin);
		String token=JWT.create().withClaim("ID", id).sign(algorithm);
		return token;
	}
	
	public static String verifyToken(String token) {
		String id;
		Verification verification=JWT.require(Algorithm.HMAC256(secretPin));
		JWTVerifier jwtVerifier=verification.build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		Claim claim=decodedJWT.getClaim("ID");
		id=claim.asString();
		return id;
	}
	
}
