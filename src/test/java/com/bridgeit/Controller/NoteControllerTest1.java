package com.bridgeit.Controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgeit.Service.CrossFilter;
import com.bridgeit.Service.NoteService;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.model.UserBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.implementation.bytecode.Throw;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class NoteControllerTest1 {
	
	  @Autowired
	    private WebApplicationContext wac;
	 
	  
	  
	  @InjectMocks
		private NoteController noteController;
	    
	  private MockMvc mockMvc;
	 
	    @Before
	    public void setup() {
	    	MockitoAnnotations.initMocks(this);
	    	mockMvc = MockMvcBuilders.standaloneSetup(noteController)
	    			.addFilter(new CrossFilter())
					.build();
	    	
	    	
	    }

	@InjectMocks
	NoteService noteService;
	
	@InjectMocks
	UserService userService;
	@InjectMocks
	TokenGenerator tokenService;
	

	@Test
	public void getNotesTest() throws Exception {

		String email="test@gmail.com";
		UserBean user = userService.getUserByEmail(email);
		String token = tokenService.createJWT(user.getId(), user.getEmail());
		HttpServletResponse response = null;
		response.setHeader("Authorization", token);
	    mockMvc.perform(get("/getNotes")
	       
	              )
	           .andExpect(status().isOk())
	    .andExpect(jsonPath("$.*", hasSize(9)))
	    .andDo(print());
	}
	
	@Test
    public void test_cors_headers() throws Exception {
        mockMvc.perform(get("/getNotes"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"))
                .andExpect(header().string("Access-Control-Allow-Headers", "*"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

}
