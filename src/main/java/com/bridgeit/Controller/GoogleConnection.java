package com.bridgeit.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleConnection {

	public static final String CLIENT_Id = "8579205163-0hqdbf87p7qfhoju7o09iodhtq589u4v.apps.googleusercontent.com";
	public static final String Secret_Id = "sesbMIF8Mq2-eWkxnt9elagx";
	public static final String Redirect_URI = "http://localhost:8080/ToDo/connectgoogle";

	// Access token in header
	public String Gmail_GET_USER_URL = "https://www.googleapis.com/plus/v1/people/me";

	public String getGoogleAuthURL(String unid) {

		String googleLoginURL = "";

		try {
			googleLoginURL = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_Id + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=" + unid
					+ "&response_type=code&scope=profile email&approval_prompt=force&access_type=offline";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("inside google authentication" + googleLoginURL);
		return googleLoginURL;
	}

	public String getAccessToken(String authCode) {

		String accessTokenURL = "https://accounts.google.com/o/oauth2/token";

		ResteasyClient restCall = new ResteasyClientBuilder().build();

		ResteasyWebTarget target = restCall.target(accessTokenURL);

		Form form = new Form();
		form.param("client_id", CLIENT_Id);
		form.param("client_secret", Secret_Id);
		form.param("redirect_uri", Redirect_URI);
		form.param("code", authCode);
		form.param("grant_type", "authorization_code");
		// using post request we are sending an data to web service and getting
		// json response back
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));

		String token = response.readEntity(String.class);
		
		ObjectMapper mapper=new ObjectMapper();
		String acc_token = null;
		try {
			acc_token = mapper.readTree(token).get("access_token").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		restCall.close();
		/*return googlemailTokens.getAccess_token();*/
		return acc_token;
	}

	public JsonNode getUserProfile(String accessToken) {

		System.out.println("gmail details " + Gmail_GET_USER_URL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(Gmail_GET_USER_URL);

		String headerAuth = "Bearer " + accessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON)
				.get();

		/*GooglePojo profile = response.readEntity(GooglePojo.class);*/
		String profile=response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		JsonNode Googleprofile = null;
		try {
			Googleprofile = mapper.readTree(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		restCall.close();
		return Googleprofile;
	}


}
