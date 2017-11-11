package com.bridgeit.Controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgeit.Service.MailImp;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.Validation.Validation;
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
    public ResponseEntity<String> createUser(@RequestBody UserBean user,UriComponentsBuilder ucBuilder,HttpServletRequest request) {
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
        			HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
                    return new ResponseEntity<String>("Inserted successfully",headers, HttpStatus.CREATED);
        		}
        	}
        	else
    		{
    			System.out.println("A User with email " + user.getEmail() + " or mobile "+user.getMobilenumber()+" already exist");
        		return new ResponseEntity<String>("User Alreay exits",HttpStatus.CONFLICT);
    		}
        }
        return new ResponseEntity<String>("Invalid data",HttpStatus.CONFLICT);
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
	public ResponseEntity<String> login(@RequestBody UserBean user,HttpServletRequest request,HttpServletResponse response)
	{
		String email= user.getEmail();
		String password=user.getPassword();
		UserBean getUser = userService.getUserByEmail(email);
		System.out.println(getUser.getPassword());
		if(getUser!=null){			
			if(BCrypt.checkpw(password,getUser.getPassword())){
				if(getUser.isActivated()){
					String token = tokenGenerator.createJWT(getUser.getId(),getUser.getPassword());
					System.out.println(token);
					response.setHeader("Authorization", token);
					/*HttpSession session = request.getSession();
					session.setAttribute(session.getId(), getUser);*/
					return ResponseEntity.ok("login Successfull");
				}else{
					return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not activated");
				}
			}else{
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid username or password");
			}
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not exists");
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

	@RequestMapping(value="/Logout",method=RequestMethod.GET)
	public ResponseEntity<String> logout(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		/*if(session.getAttribute(session.getId())==null)*/
		if(request.getHeader("Authorization").isEmpty())
		{
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Login first"); 
		}else{
			/*session.removeAttribute(session.getId());*/
			/*response.getHeader("Authorization").replace("Authorization", "").trim();*/
			request.getHeader("Authorization").trim();
			return ResponseEntity.ok().body("Logout Successfull");
		}
	}
	
   
	
}
