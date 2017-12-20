package com.bridgeit.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class FacebookConnection {

	public static final String app_Id = "133399100756118";
	public static final String secret_id = "dabdc27c904e1e48a5fc2deec583269b";
	public static final String Redirect_URI = "http://localhost:8080/ToDo/connectFB";
	
	
	public String getFacebookURL(String uuid) {
		String facebookLoginURL="";
		try{
			facebookLoginURL= "http://www.facebook.com/dialog/oauth?" + "client_id=" + app_Id + "&redirect_uri="
					+ URLEncoder.encode(Redirect_URI, "UTF-8") + "&state=" + uuid + "&response_type=code"
					+ "&scope=public_profile,email";
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("inside google authentication" + facebookLoginURL);
		return facebookLoginURL;
	}

	public String getAccessToken(String authCode) throws UnsupportedEncodingException {
		String fbAccessTokenURL = "https://graph.facebook.com/v2.9/oauth/access_token?" + "client_id=" + app_Id
				+ "&redirect_uri=" + URLEncoder.encode(Redirect_URI, "UTF-8") + "&client_secret=" + secret_id + "&code="
				+ authCode;
		ResteasyClient restCall =  new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(fbAccessTokenURL);
		Form form = new Form();
		
		form.param("client_id", app_Id);
		form.param("client_secret", secret_id);
		form.param("redirect_uri", Redirect_URI);
		form.param("code", authCode);
		form.param("grant_type", "authorization_code");
		
		Response response = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(form));
		System.out.println("Response::"+response);
		String facebookAccessToken = response.readEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		String acc_token=null;
		try {
			acc_token = mapper.readTree(facebookAccessToken).get("access_token").asText();
		} catch (Exception e) {
				e.printStackTrace();
		}
		return acc_token;
	}

	public JsonNode getUserProfile(String fbAccessToken) {
		
		String fbgetUserURL = "https://graph.facebook.com/v2.9/me?access_token=" + fbAccessToken
				+ "&fields=id,name,email,picture";
		System.out.println("fb get user details " + fbgetUserURL);
		ResteasyClient restCall = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = restCall.target(fbgetUserURL);

		String headerAuth = "Bearer " + fbAccessToken;
		Response response = target.request().header("Authorization", headerAuth).accept(MediaType.APPLICATION_JSON).get();
		String profile =  response.readEntity(String.class);
		ObjectMapper mapper=new ObjectMapper();
		
		JsonNode FBprofile = null;
		try {
			FBprofile = mapper.readTree(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		restCall.close();
		return FBprofile;
	}



}
