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
import com.bridgeit.Service.Producer;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.model.Email;
import com.bridgeit.model.ResponseMessage;
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
	
	@Autowired
	Producer producer;
	 //-------------------Forgot password--------------------------------------------------------

		@RequestMapping(value="/forgotpassword",method=RequestMethod.POST)
		public ResponseEntity<ResponseMessage> forgot(@RequestBody UserBean user,HttpServletResponse response,HttpServletRequest request/*,UriComponentsBuilder ucBuilder*/)
		{
			ResponseMessage responseMessage = new ResponseMessage();
			String email = user.getEmail();
			user = userService.getUserByEmail(email);
			if(user!=null)
			{
				String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
				String url = String.valueOf(request.getRequestURL());
				System.out.println(url);
				
				url = "http://localhost:8080/ToDo/#!/resetPassword/"+token;
				System.out.println(request.getScheme()+" "+request.getAuthType()+" "+request.getContentType()+" "+request.getContextPath()+" "+request.getLocalPort());
				System.out.println(url);
				
				
				Email emailObject = new Email();
				emailObject.setTo(email);
				emailObject.setSubject("Reset password link");
				emailObject.setBody(url);	
				// Storing message in JMS queue
    			producer.send(emailObject);

    			responseMessage.setResponseMessage("Check your Email!");
    			return ResponseEntity.ok(responseMessage);
				
			}else{
				responseMessage.setResponseMessage("User Does not exist");
				return ResponseEntity.ok(responseMessage);
			}
		}
		
		
		@RequestMapping(value="/resetPassword/{token:.+}",method=RequestMethod.POST)
		public ResponseEntity resetpass(@PathVariable("token") String token,@RequestBody UserBean user)
		{
			ResponseMessage responseMessage = new ResponseMessage();
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
				responseMessage.setResponseMessage("password changed");
				return ResponseEntity.ok(responseMessage);
			}
			else{
				responseMessage.setResponseMessage("Invalid token");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
			}
		}

}
