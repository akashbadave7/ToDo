package com.bridgeit.Token;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.model.ResponseMessage;

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
			System.out.println(Id);
		} catch (MissingClaimException e) {
			e.printStackTrace();
			
			
			return Id;
		} catch (Exception e1) {
			e1.printStackTrace();
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