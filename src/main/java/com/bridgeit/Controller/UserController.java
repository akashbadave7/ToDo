package com.bridgeit.Controller;

import java.io.IOException;
import java.util.List;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgeit.Service.Producer;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.Validation.Validation;
import com.bridgeit.model.Email;
import com.bridgeit.model.ResponseMessage;
import com.bridgeit.model.UserBean;


@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	TokenGenerator tokenGenerator;
	@Autowired
	Validation valid;
	@Autowired
	VerifyToken verifyToken;
	
	@Autowired
	Producer producer;

	
	private static final Logger logger = Logger.getLogger("loginFile");
	private static final Logger logger1 = Logger.getRootLogger();
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public ResponseEntity<UserBean> user(@PathVariable ("id") int id)
	{
		ResponseMessage message = new ResponseMessage();
		System.out.println(id);
		System.out.println(userService);
		UserBean user = userService.getUserById(id);
		if(user==null) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		System.out.println(user.getEmail());
		return ResponseEntity.ok(user);
	}
    //-------------------Retrieve Single User--------------------------------------------------------

	@RequestMapping(value="/getUser",method=RequestMethod.GET)
	public ResponseEntity<UserBean> getUser(HttpServletRequest request) {
		
		String token= request.getHeader("Authorization");
		 UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		 if (user!=null) {
			 	return ResponseEntity.ok(user);
	        }else {
	        	 return ResponseEntity.ok(user);
	        }
		
	}
	
    //-------------------Insert a User--------------------------------------------------------

	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createUser(@RequestBody UserBean user,UriComponentsBuilder ucBuilder,HttpServletRequest request) throws JMSException {
		ResponseMessage errorMessage = new ResponseMessage();
		System.out.println("Creating User " + user.getName());
		System.out.println(user);
        if(valid.signUpValidator(user))
        {
        	// setting user activation false bydefault
        	user.setActivated(true);
        	if (userService.isUserExits(user.getEmail(),user.getMobilenumber())) 
        	{	
        		int i= userService.saveUserData(user);
        		if(i>0)
        		{
        			String token = tokenGenerator.createJWT(user.getId(),null);
        			String url = String.valueOf(request.getRequestURL());
        			url = url.substring(0,url.lastIndexOf("/"))+"/activate/"+token;
        			
        			Email email = new Email();
    				email.setTo(user.getEmail());
    				email.setSubject("Confirmation email");
    				email.setBody(url);	
    				// Storing message in JMS queue
        			producer.send(email);
        			System.out.println(email);
        			/*System.out.println("Verification mail sent");*/
        			HttpHeaders headers = new HttpHeaders();
                    /*headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());*/
                    logger.info("Registration successfull");
                   /* System.out.println("Registration successfull");*/
                    errorMessage.setResponseMessage("Registration successfull");
                    return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
        		}
        	}
        	else
    		{
        		logger.info("User already exits");
    			System.out.println("A User with email " + user.getEmail() + " or mobile "+user.getMobilenumber()+" already exist");
    			errorMessage.setResponseMessage("Email or Mobile number already register.");
    			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    		}
        }
        System.out.println("Error occured");
        logger.warn("Error occure");
        errorMessage.setResponseMessage("Error Occured");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
	
    //-------------------Activate a User--------------------------------------------------------
	@RequestMapping(value="/activate/{token:.+}",method=RequestMethod.GET)
	public ResponseMessage activateUser(@PathVariable("token") String token,HttpServletResponse response,HttpServletRequest request){
		int id = verifyToken.parseJWT(token);
		ResponseMessage errorMessage = new ResponseMessage();
		System.out.println(id);
		if(id>0){
			UserBean user = userService.getUserById(id);
			if(user!=null){
				user.setActivated(true);
				if(userService.updateUser(user)){
			
						try {
							response.sendRedirect("http://localhost:8080/ToDo/#!/login");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						/*response.sendRedirect("#!/login");*/
					
					errorMessage.setResponseMessage("Activation successfull");
					return errorMessage;
				}else{
					errorMessage.setResponseMessage("Error in activation");
					return errorMessage;
				}
			}else{
				errorMessage.setResponseMessage("User does not exist");
				return errorMessage;
			}
		}else{
			errorMessage.setResponseMessage("Token Expire or Invalid");
			return errorMessage;
		}
		
	}
    //-------------------Login a User--------------------------------------------------------

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<ResponseMessage> login(@RequestBody UserBean user,HttpServletRequest request,HttpServletResponse response)
	{
		String email= user.getEmail();
		String password=user.getPassword();
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
		ResponseMessage errorMessage = new ResponseMessage();
		UserBean getUser = userService.getUserByEmail(email);
		
		
		if(getUser == null){
			errorMessage.setResponseMessage("Email does not exists try again");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
			else{	
				System.out.println(getUser.getEmail());
			if(BCrypt.checkpw(password,getUser.getPassword())){
				if(getUser.isActivated()){
					String token = tokenGenerator.createJWT(getUser.getId(),getUser.getPassword());
					System.out.println(token);
					response.setHeader("Authorization", token);
					/*HttpSession session = request.getSession();
					session.setAttribute(session.getId(), getUser);*/
					logger.info("Login Successfull");
					errorMessage.setResponseMessage(token);
					return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
				}else{
					logger.info("User not activated");
					errorMessage.setResponseMessage("User not activated");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
			}else{
				logger.info("Invalid password or email");
				errorMessage.setResponseMessage("Wrong Password");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}
		}
	}
	
	@RequestMapping(value="/isActive",method=RequestMethod.GET)
	public ResponseEntity<Object> isActivated(HttpSession session)
	{
		UserBean user = (UserBean) session.getAttribute(session.getId());
		if(user!=null){
			return ResponseEntity.ok().body(user);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("No user logged in.");
		}
	}
	
    //-------------------Login a User--------------------------------------------------------

	@RequestMapping(value="/Logout",method=RequestMethod.POST)
	public void logout(@RequestHeader(value="token") String header,HttpServletRequest request){
	
			int id = verifyToken.parseJWT(header);
			/*UserBean user = userService.getUserById(id);*/
			request.removeAttribute(header);
	}
	
	
	@RequestMapping(value="/getUsers/{keyword}" ,method=RequestMethod.GET)
	public ResponseEntity<List<String>> getUsers(@RequestHeader(value="token") String header, @PathVariable("keyword") String keyword,HttpServletRequest request){
		//ResponseMessage responseMessage = new ResponseMessage();
		String token = request.getHeader("Authorization");
		List<String> emails= null;
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		
		if(user!=null)
		{
			emails = userService.getUsers(keyword);
		}
		else{
			//responseMessage.setResponseMessage("User Not Logged In");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(emails, HttpStatus.OK);
	}
	
    //-------------------GET ALL USERS--------------------------------------------------------

	@RequestMapping(value = "/getUserList/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<UserBean>> getUserList(@PathVariable ("id") int id,HttpServletRequest request){
		String token =request.getHeader("Authorization");
		/*UserBean user=userService.getUserById(verifyToken.parseJWT(token));*/
		UserBean user=userService.getUserById(id);
		if(user!=null){
			List<UserBean> list=userService.getUserList();
			return ResponseEntity.ok(list);
		}else{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
	}
	
	@RequestMapping(value="/updateUser",method=RequestMethod.POST)
	public ResponseEntity<ResponseMessage> updateUser(@RequestBody UserBean user,HttpServletRequest request,HttpServletResponse response)
	{
		 ResponseMessage responseMessage = new ResponseMessage();
		 String token = request.getHeader("Authorization");  
		 UserBean userProfile = userService.getUserById(verifyToken.parseJWT(token));
		 if(userProfile!=null){
			userProfile.setPicUrl(user.getPicUrl());
			userService.updateUser(userProfile);
		 }else{
			 responseMessage.setResponseMessage("User Not logged in");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
		 }
		 responseMessage.setResponseMessage("Successfull");
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	
}
