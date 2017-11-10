package com.bridgeit.Controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
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
	public void redirectURL(HttpServletRequest request,HttpServletResponse response,UriComponentsBuilder ucBuilder) throws IOException
	{
		String sessionState = (String) request.getSession().getAttribute("State");
		String googlestate = request.getParameter("state");
		System.out.println("in connect facebook");
		
		if(sessionState==null || !sessionState.equals(googlestate))
		{
			response.sendRedirect("facebookLogin");
		}

		String error = request.getParameter("error");
		if (error != null && error.trim().isEmpty()) {
			response.sendRedirect("login");
		}
		
		String authCode = request.getParameter("code");
		String fbAccessToken = connection.getAccessToken(authCode);
		System.out.println("***fbaccessToken ***" + fbAccessToken);
		JsonNode profile = connection.getUserProfile(fbAccessToken);
		System.out.println("fb profile :" + profile);
		System.out.println(profile.get("email").asText());
		String email= profile.get("email").asText();
		UserBean user = userService.getUserByEmail(email);
		System.out.println("fb img "+ profile.get("picture").get("data").get("url").asText());
		if(user==null) {
			System.out.println("User is not register before");
			user = new UserBean();
			user.setName(profile.get("name").asText());
			user.setEmail(profile.get("email").asText());
			/*BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(profile.get("password").asText()));*/
		
			user.setPassword("");
			user.setActivated(true);
			userService.saveUserData(user);
			/*if(i>0) {
				String token = tokenGenerator.createJWT(user.getId(),null);
    			String url = String.valueOf(request.getRequestURL());
    			url = url.substring(0,url.lastIndexOf("/"))+"/activate/"+token;
    			System.out.println(url);
    			sendMail.sendMail(user.getEmail(), url,"Confirmation email");
    			HttpHeaders headers = new HttpHeaders();
                headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
                return new ResponseEntity<String>("Inserted successfully",headers, HttpStatus.CREATED);
			}*/
		}else {
		System.out.println(" user is not new to our db ,it is there in our db");
		}
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		/*user.setProfile(profile.get("picture").get("data").get("url").asText());
		userService.updateUserProfile(user);

		tokens.setGetUser(user);
		tokenService.saveToken(tokens);
		 */
		Cookie acccookie = new Cookie("socialaccessToken", token);
		Cookie refreshcookie = new Cookie("socialrefreshToken", token);
		response.addCookie(acccookie);
		response.addCookie(refreshcookie);
		response.sendRedirect("http://localhost:8080/ToDo/#!/index");
	}


}
