package com.bridgelabz.fundoo.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

	

@Component
public class TokenUtility implements ITokenGenerator{
	private static String secretPin="flash908642018";
	
	@Override
	public String generateToken(String id) {
//		Algorithm algorithm=Algorithm.HMAC256(secretPin);
//		String token=JWT.create().withClaim("ID", id).sign(algorithm);
//		return token;
		
		Calendar calendar=Calendar.getInstance(Locale.US);
		Calendar calendar1=Calendar.getInstance(Locale.US);
		calendar1.setTime(calendar.getTime());
		calendar1.add(Calendar.HOUR_OF_DAY, 8);
		String token=Jwts.builder().setSubject("FundooNotes").setIssuedAt(calendar.getTime())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)).setId(id)
				.signWith(SignatureAlgorithm.HS256, secretPin).compact();
		return token;
		
	}
	
	
	@Override
	public  String verifyToken(String token) {
//		String id;
//		Verification verification=JWT.require(Algorithm.HMAC256(secretPin));
//		JWTVerifier jwtVerifier=verification.build();
//		DecodedJWT decodedJWT = jwtVerifier.verify(token);
//		Claim claim=decodedJWT.getClaim("ID");
//		id=claim.asString();
//		return id;
		
		Jws<Claims> claims=Jwts.parser().setSigningKey(secretPin).parseClaimsJws(token);
		String userId=	claims.getBody().getId();
		return userId;
	}
	
}
