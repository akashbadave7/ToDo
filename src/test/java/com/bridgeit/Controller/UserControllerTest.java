package com.bridgeit.Controller;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.model.UserBean;
import com.fasterxml.jackson.databind.ObjectMapper;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-servlet.xml"})
@WebAppConfiguration
public class UserControllerTest {


	@InjectMocks
    private UserController userController;

	
    private MockMvc mockMvc;
	
    @Autowired
    @Spy
    private UserService userService;
    
    @Autowired
    @Spy
	TokenGenerator tokenGenerator;
    
    @Autowired
    @Spy
    VerifyToken verifyToken;
    /*@Mock
    private UserDaoImp userDaoImp;*/
    
    private ObjectMapper mapper;

	

   

    @Before
    public void setup() {

        // this must be called for the @Mock annotations above to be processed
        // and for the mock service to be injected into the controller under
        // test.
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

	@Test
	public void getUserById() throws Exception {
		 int userId = 4;
		 mockMvc.perform(
	                 MockMvcRequestBuilders.get("/user/" + userId).contentType(MediaType.APPLICATION_JSON));
		 				/*.andExpect(status().isOk());*/	
	    }
	
	@Test
	public void loginUser() throws Exception {
		/*String email="akash.badave@gmail.com";
		String password ="akash123";*/
		UserBean user = new UserBean();
		user.setEmail("akash.badave7@gmail.com");
		user.setPassword("akash123");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(asJsonString(user)));
			       /*.andExpect(status().isOk());*/
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