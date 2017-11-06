package com.bridgeit.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgeit.Service.MailImp;
import com.bridgeit.Service.UserService;
import com.bridgeit.Validation.Validation;
import com.bridgeit.model.UserBean;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailImp sendMail;
	@Autowired
	Validation valid;
	
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

	@RequestMapping(value = "/userResister", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody UserBean user,UriComponentsBuilder ucBuilder,HttpServletRequest request) {
		System.out.println("Creating User " + user.getName());
        if(valid.signUpValidator(user))
        {
        	user.setActivated(false);
        	if (userService.isUserExits(user)) 
        	{	
        		int i= userService.saveUserData(user);
        		if(i>0)
        		{
        			String url = String.valueOf(request.getRequestURL());
        			url = url.substring(0,url.lastIndexOf("/"))+"/activate/"+i;
        			System.out.println(url);
        			sendMail.sendMail(user.getEmail(), url);
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
        return new ResponseEntity<String>(HttpStatus.CONFLICT);
	}
	
    //-------------------Activate a User--------------------------------------------------------
	@RequestMapping(value="/activate/{id}",method=RequestMethod.GET)
	public ResponseEntity activateUser(@PathVariable("id") int id){
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
	}
    //-------------------Login a User--------------------------------------------------------
	@RequestMapping(value="/Login",method=RequestMethod.POST)
	public ResponseEntity login(@RequestBody UserBean user,HttpServletRequest request)
	{
		String email= user.getEmail();
		String password = user.getPassword();
		UserBean getUser = userService.getUserByEmail(user);
		if(getUser!=null){
			if(getUser.getPassword().equals(password)){
				if(getUser.isActivated()){
					System.out.println(getUser.getEmail());
					HttpSession session = request.getSession();
					session.setAttribute(session.getId(), getUser);
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
	public ResponseEntity isActivated(HttpSession session)
	{
		UserBean user = (UserBean) session.getAttribute(session.getId());
		if(user!=null){
			return ResponseEntity.ok().body(user);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("No user logged in.");
		}
	}
	
    //-------------------Login a User--------------------------------------------------------

	@RequestMapping(value="/LogOut",method=RequestMethod.GET)
	public ResponseEntity logout(HttpSession session){
		session.removeAttribute(session.getId());
		return ResponseEntity.ok().body("Logout Successfull");
	}
}
