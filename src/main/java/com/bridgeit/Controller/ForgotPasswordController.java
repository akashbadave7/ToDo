package com.bridgeit.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.Service.MailImp;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;

import com.bridgeit.model.UserBean;

@RestController
public class ForgotPasswordController {
	
	@Autowired
	private MailImp sendMail;
	@Autowired
	TokenGenerator tokenGenerator;
	@Autowired
	VerifyToken verifyToken;
	@Autowired
	UserService userService;
	
	 //-------------------Forgot password--------------------------------------------------------

		@RequestMapping(value="/forgotpass",method=RequestMethod.POST)
		public ResponseEntity<String> forgot(@RequestBody UserBean user,HttpServletResponse response,HttpServletRequest request/*,UriComponentsBuilder ucBuilder*/)
		{
			String email = user.getEmail();
			user = userService.getUserByEmail(email);
			if(user!=null)
			{
				String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
				String url = String.valueOf(request.getRequestURL());
				url = "http://localhost:8080/ToDo/resetpass/"+token;
				System.out.println(url);
				sendMail.sendMail(email, url,"Reset password link");
				response.setHeader("Location", "http://localhost:8080/ToDo");
				/*HttpHeaders headers = new HttpHeaders();
	            headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
	            return new ResponseEntity<String>("Inserted successfully",headers, HttpStatus.CREATED);*/
				return ResponseEntity.ok().body("ok");
				
			}else{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not found");
			}
		}
		
		
		@RequestMapping(value="/resetpass/{token:.+}",method=RequestMethod.POST)
		public ResponseEntity resetpass(@PathVariable("token") String token,@RequestBody UserBean user)
		{
			int id = verifyToken.parseJWT(token);
			String newPassword = user.getPassword();
			if(id!=0)
			{
				System.out.println(user.getPassword());
				
				user = userService.getUserById(id);
				/*String email= user.getEmail();*/
				BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
				user.setPassword(encoder.encode(newPassword));
				/*user.setPassword(newPassword);*/
				userService.updateUser(user);
				return ResponseEntity.ok().body("Password reset succefull");
			}
			else{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or expire token");
			}
		}

}
