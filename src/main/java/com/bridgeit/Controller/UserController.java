package com.bridgeit.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Request;

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

import com.bridgeit.Service.MailImp;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.Validation.Validation;
import com.bridgeit.model.ErrorMessage;
import com.bridgeit.model.UserBean;


@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailImp sendMail;
	@Autowired
	TokenGenerator tokenGenerator;
	@Autowired
	Validation valid;
	@Autowired
	VerifyToken verifyToken;
	
	private static final Logger logger = Logger.getLogger("loginFile");
	private static final Logger logger1 = Logger.getRootLogger();
    //-------------------Retrieve Single User--------------------------------------------------------

	@RequestMapping(value="/getUser/{id}",method=RequestMethod.GET)
	public ResponseEntity<UserBean> getUserById(@PathVariable("id") int id) {
		 System.out.println("Fetching User with id " + id);
		 UserBean user = userService.getUserById(id);
		 if (user == null) {
	            System.out.println("User with id " + id + " not found");
	            return new ResponseEntity<UserBean>(HttpStatus.BAD_GATEWAY);
	        }
	        return new ResponseEntity<UserBean>(user, HttpStatus.OK);
	}
	
    //-------------------Insert a User--------------------------------------------------------

	@RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    public ResponseEntity<ErrorMessage> createUser(@RequestBody UserBean user,UriComponentsBuilder ucBuilder,HttpServletRequest request) {
		ErrorMessage errorMessage = new ErrorMessage();
		System.out.println("Creating User " + user.getName());
        if(valid.signUpValidator(user))
        {
        	user.setActivated(false);
        	if (userService.isUserExits(user.getEmail(),user.getMobilenumber())) 
        	{	
        		int i= userService.saveUserData(user);
        		if(i>0)
        		{
        			String token = tokenGenerator.createJWT(user.getId(),null);
        			String url = String.valueOf(request.getRequestURL());
        			url = url.substring(0,url.lastIndexOf("/"))+"/activate/"+token;
        			System.out.println(url);
        			sendMail.sendMail(user.getEmail(), url,"Confirmation email");
        			System.out.println("Verification mail sent");
        			HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
                    logger.info("Registration successfull");
                    System.out.println("Registration successfull");
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
	public ResponseEntity<String> activateUser(@PathVariable("token") String token){
		int id = verifyToken.parseJWT(token);
		System.out.println(id);
		if(id>0){
			UserBean user = userService.getUserById(id);
			if(user!=null){
				user.setActivated(true);
				if(userService.updateUser(user)){
					return ResponseEntity.ok("Activated successfull");
				}else{
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in activation");
				}
			}else{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
			}
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Expire or Invalid");
		}
	}
    //-------------------Login a User--------------------------------------------------------

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<ErrorMessage> login(@RequestBody UserBean user,HttpServletRequest request,HttpServletResponse response)
	{
		String email= user.getEmail();
		String password=user.getPassword();
		ErrorMessage errorMessage = new ErrorMessage();
		UserBean getUser = userService.getUserByEmail(email);
		
		if(getUser == null){
			errorMessage.setResponseMessage("Email does not exists try again");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
			else{		
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
				errorMessage.setResponseMessage("Invalid password or email");
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
}
