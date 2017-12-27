package com.bridgeit.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.model.ResponseMessage;
import com.bridgeit.model.UserBean;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class FacebookController {

	@Autowired
	FacebookConnection connection;
	@Autowired
	UserService userService;
	@Autowired
	TokenGenerator tokenGenerator;
	private static final Logger logger = Logger.getLogger("loginFile");
	@RequestMapping(value="/facebookLogin")
	public void facebookConnection(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		System.out.println("FaceBook login");
		String uuid = UUID.randomUUID().toString();
		request.getSession().setAttribute("State", uuid);
		String facebookLoginURL = connection.getFacebookURL(uuid);
		System.out.println("facebookLoginURL  " +facebookLoginURL);
		response.sendRedirect(facebookLoginURL);
	}
	
	@RequestMapping(value="/connectFB")
	public void redirectURL(HttpServletRequest request,HttpServletResponse response,HttpSession session,UriComponentsBuilder ucBuilder) throws IOException
	{
		ResponseMessage errorMessage = new ResponseMessage();
		String sessionState = (String) request.getSession().getAttribute("State");
		String googlestate = request.getParameter("state");
		System.out.println("in connect facebook");
		
		if(sessionState==null || !sessionState.equals(googlestate))
		{
			response.sendRedirect("facebookLogin");
		}

		String error = request.getParameter("error");
		if (error != null && error.trim().isEmpty()) {
			errorMessage.setResponseMessage("Error occured Try again.");
		}
		
		String authCode = request.getParameter("code");
		String fbAccessToken = connection.getAccessToken(authCode);
		System.out.println("***fbaccessToken ***" + fbAccessToken);
		JsonNode profile = connection.getUserProfile(fbAccessToken);
		System.out.println("fb profile :" + profile);
		System.out.println(profile.get("email").asText());
		String email= profile.get("email").asText();
		UserBean user = userService.getUserByEmail(email);
		
		if(user==null) {
			System.out.println("User is not register before");
			user = new UserBean();
			user.setName(profile.get("name").asText());
			user.setEmail(profile.get("email").asText());
			/*BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(profile.get("password").asText()));*/
		
			user.setPassword("");
			user.setActivated(true);
			user.setPicUrl(profile.get("picture").get("data").get("url").asText());
			int i=userService.saveUserData(user);
			if(i>0) {
				String token = tokenGenerator.createJWT(user.getId(),user.getEmail());
    			response.setHeader("Authorization", token);
    			session.setAttribute("token", token);
    			response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
			}
			else
			{
				response.sendRedirect("http://localhost:8080/ToDo/#!/login");
			}
		}else {
			logger.warn("User data already exists in database");
			System.out.println(" user is not new to our db ,it is there in our db");
			String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
			user.setPicUrl(profile.get("picture").get("data").get("url").asText());
			userService.updateUser(user);
			session.setAttribute("token", token);
			response.sendRedirect("http://localhost:8080/ToDo/#!/dummy");
		}
		
	}
	
	@RequestMapping(value="/gettoken")
	public ResponseEntity<ResponseMessage> getToken(HttpSession session){
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setResponseMessage((String) session.getAttribute("token"));
		session.removeAttribute("token");
		return  ResponseEntity.ok(responseMessage);
	}
}
