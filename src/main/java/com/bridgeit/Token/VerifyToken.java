package com.bridgeit.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;

public class VerifyToken {

	String keyvariable = "secreateKey";

	public int parseJWT(String jwt) {
		int Id = 0;

		try {
			Claims claims = Jwts.parser().setSigningKey(keyvariable).parseClaimsJws(jwt).getBody();
			Id = Integer.parseInt(claims.getId());
		} catch (MissingClaimException e) {
			System.out.println("missing claims " + e);
			return Id;
		} catch (IncorrectClaimException e1) {
			System.out.println("Incorrect claims " + e1);
			return Id;
		}

		return Id;

	}

	public String parseString(String jwt) {
		String word = null;

		try {
			Claims claims = Jwts.parser().setSigningKey(keyvariable).parseClaimsJws(jwt).getBody();
			word = claims.getSubject();
		} catch (MissingClaimException e) {
			System.out.println("missing claims " + e);
			return word;
		} catch (IncorrectClaimException e1) {
			System.out.println("Incorrect claims " + e1);
			return word;
		}
		return word;

	}

}