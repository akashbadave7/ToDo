package com.bridgeit.Controller;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.model.UserBean;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * @author bridgeit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-servlet.xml"})
@WebAppConfiguration
public class UserControllerTest {


	@InjectMocks
    private UserController userController;

    @Autowired
    @Spy
	TokenGenerator tokenGenerator;
     

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before 
    public void setUp() throws Exception {
        /*MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .build();*/
    	 this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    
    /*=================================Login User Test Cases================================================*/
    
    /**
     * @throws Exception
     * Test case for succcessfull login user
     */
    @Test
	//@Ignore
	public void loginUserSuccessfull() throws Exception {
		/*String email="akash.badave@gmail.com";
		String password ="akash123";*/
		UserBean user = new UserBean();
		user.setEmail("akash.badave7@gmail.com");
		user.setPassword("akash123");
		
		mockMvc.perform(post("/login")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
			       .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
				   .andDo(MockMvcResultHandlers.print())
				   .andExpect(status().isOk());
	}
    
    
    /**
     * @throws Exception
     * Test case to check login with  wrong password
     */
    @Test
	//@Ignore
	public void loginUserWrongPassword() throws Exception {
		UserBean user = new UserBean();
		user.setEmail("akash.badave7@gmail.com");
		user.setPassword("akash1234");
		
		mockMvc.perform(post("/login")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
			       .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
				   .andDo(MockMvcResultHandlers.print())
				   .andExpect(jsonPath("$.responseMessage", Matchers.is("Wrong Password")))
				   .andExpect(status().isConflict());
	}
	
    /**
     * @throws Exception
     * Test case to login with invalid email
     */
    @Test
	//@Ignore
	public void loginUserEmailNotExits() throws Exception {
		UserBean user = new UserBean();
		user.setEmail("akash.badave77@gmail.com");
		user.setPassword("akash123");
		
		mockMvc.perform(post("/login")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
			       .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
				   .andDo(MockMvcResultHandlers.print())
				   .andExpect(jsonPath("$.responseMessage", Matchers.is("Email does not exists try again")))
				   .andExpect(status().isBadRequest());
	}
	
    
    /**
     * @throws Exception
     * Test case for login with user which is not activated
     */
    @Test
    //@Ignore
   	public void loginUserNotActivated() throws Exception {
   		UserBean user = new UserBean();
   		user.setEmail("test@gmail.com");
   		user.setPassword("akash123");
   		
   		mockMvc.perform(post("/login")
   			       .contentType(MediaType.APPLICATION_JSON)
   			       .content(asJsonString(user)))
   			       .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
   				   .andDo(MockMvcResultHandlers.print())
   				   .andExpect(jsonPath("$.responseMessage", Matchers.is("User not activated")))
   				   .andExpect(status().isExpectationFailed());
   	}
    

    /*=================================Fetch user Details Test Cases================================================*/

    
	/**
	 * @throws Exception
	 * Test Case to fetch user with valid user email id
	 */
	@Test
	//@Ignore
	public void getUserByEmail() throws Exception {
		 String email = "akash.badave7@gmail.com";
		 
		mockMvc.perform(get("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.param("email", email))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.email", Matchers.is("akash.badave7@gmail.com")))
				.andExpect(status().isOk());
	
	    }
	
	
	/**
	 * @throws Exception
	 * Test case to fetch user with invalid email id
	 */
	@Test
	//@Ignore
	public void getUserByIvalidEmail() throws Exception {
		 String email = "akash.badave7475@gmail.com";
		 
		mockMvc.perform(get("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.param("email", email))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.responseMessage", Matchers.is("User not found!!")))
				.andExpect(status().isNotFound());
	
	    }
	
    /*=================================Register user Test Cases================================================*/

	/**
	 * @throws Exception
	 * Test case for regiration with validation fail
	 */
	@Test
	//@Ignore
	public void register() throws Exception {
		UserBean user = new UserBean();
		user.setEmail("akashfssdfdsf.com");
		user.setPassword("akash123");
		user.setMobilenumber("4578987458");
		user.setName("Abc");
		mockMvc.perform(post("/userRegister")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
				   .andExpect(jsonPath("$.responseMessage", Matchers.is("Enter valid data")))
			       .andExpect(status().isNotAcceptable());
	}
	
	/**
	 * @throws Exception
	 * Test case to register user with already register with email
	 */
	@Test
	//@Ignore
	public void registeUserAlreadyExits() throws Exception {
		UserBean user = new UserBean();
		user.setEmail("akash.badave7@gmail.com");
		user.setPassword("akash123");
		user.setMobilenumber("8147901147");
		user.setName("Abc");
		mockMvc.perform(post("/userRegister")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
				   .andExpect(jsonPath("$.responseMessage", Matchers.is("Email or Mobile number already register.")))
			       .andExpect(status().isConflict());
	}
	
	/**
	 * @throws Exception
	 * Test Case for registering valid user
	 */
	@Test
	//@Ignore
	public void registeValidUser() throws Exception {
		UserBean user = new UserBean();
		user.setEmail("sadasdasd@gmail.com");
		user.setPassword("akash123");
		user.setMobilenumber("8143251147");
		user.setName("Akash");
		mockMvc.perform(post("/userRegister")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)))
				   .andExpect(jsonPath("$.responseMessage", Matchers.is("Registration successfull")))
			       .andExpect(status().isOk());
	}
	
    /*=================================Logout user Test Cases================================================*/

	/**
	 * @throws Exception
	 * Test case to logout user
	 */
	@Test
	//@Ignore
	public void logout() throws Exception {
		UserBean user = new UserBean();
		user.setId(10);
		user.setEmail("hello@gmail.com");
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		
		mockMvc.perform(post("/Logout")
				.header("Authorization", token) 
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
    /*=================================Get All users Test Cases================================================*/

	/**
	 * @throws Exception
	 * Test case to get all users from Database
	 */
	@Test
	//@Ignore
	public void getAllUser() throws Exception{
		UserBean user = new UserBean();
		user.setId(4);
		user.setEmail("akash.badave7@gmail.com");
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		
		mockMvc.perform(get("/getUserList")
				.header("Authorization", token) 
				.contentType(MediaType.APPLICATION_JSON))
				/*.andExpect(jsonPath("$", Matchers.hasSize(4)))*/
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}
	
	@Test
	//@Ignore
	public void getAllUserInvalid() throws Exception{
		UserBean user = new UserBean();
		user.setId(15);
		user.setEmail("akash.baasdasd@gmail.com");
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		
		mockMvc.perform(get("/getUserList")
				.header("Authorization", token) 
				.contentType(MediaType.APPLICATION_JSON))
				/*.andExpect(jsonPath("$", Matchers.hasSize(4)))*/
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized());
	}
	
	
    /*=================================Update user users Test Cases================================================*/

	/**
	 * @throws Exception
	 * Test case for updating user who has not logged in
	 */
	@Test
	////@Ignore
	public void updateUserFailed() throws Exception{
		UserBean user = new UserBean();
		user.setId(10);
		user.setEmail("unknownasdasd@gmail.com");
		user.setName("juni test");
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		mockMvc.perform(post("/updateUser")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)))
				.andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized());
	}
	
	
	/**
	 * @throws Exception
	 * Test case for updating user 
	 */
	@Test
	//@Ignore
	public void updateUser() throws Exception{
		UserBean user = new UserBean();
		user.setId(5);
		user.setEmail("test@gmail.com");
		user.setName("Junit tesaasdsdting");
		String token = tokenGenerator.createJWT(user.getId(), user.getEmail());
		
		mockMvc.perform(post("/updateUser")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)))
/*				.andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
*/				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}
	
	 public static String asJsonString(final Object obj) {
	        try {
	            final ObjectMapper mapper = new ObjectMapper();
	            return mapper.writeValueAsString(obj);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	 
	 

}
