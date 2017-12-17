package com.bridgeit.Controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;

import com.bridgeit.Service.NoteService;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.model.NoteBean;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-servlet.xml"})
@WebAppConfiguration
public class NoteControllerTest {

	 	@InjectMocks
	    private NoteController noteController;

	    @Autowired
	    @Spy
	    private NoteService noteService;

	    @Autowired
	    @Spy
	    private TokenGenerator tokenService;
	 /*   @Mock
	    View mockView;*/

	    @Autowired
	    @Spy
	    private UserService userService;
	    
	    @Autowired
	    @Spy
		private VerifyToken verifyToken;
	    
	   private MockMvc mockMvc;

	    @Before 
	    public void setUp() throws Exception {
	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
	                
	                .build();
	    }

	    @Test
	    @Ignore
	    public void getNotes() throws Exception {
	       /* List<NoteBean> expectedPeople = asList(new NoteBean());
	        when(noteService.getAllNotes(user)("someGroup")).thenReturn(expectedPeople);
*/				System.out.println(mockMvc);
	        mockMvc.perform(MockMvcRequestBuilders.get("/getNotes"))
	                .andExpect(status().isOk());
	    }
	    
	    
	    @Test
	    @Ignore
	    public void update() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 1;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	note.setTitle("Jnuit");
	    	note.setBody("Test");
	    	mockMvc.perform(MockMvcRequestBuilders.post("/update")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    @Ignore
	    public void addNote() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 1;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	note.setTitle("Add note");
	    	note.setBody("Using Junit");
	    	mockMvc.perform(MockMvcRequestBuilders.post("/addNote")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }

	    @Test
	    @Ignore
	    public void deleteNoteById() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 1;
	    	String token=tokenService.createJWT(id, email);
	    	int noteId=5;
	    	
	    	mockMvc.perform(MockMvcRequestBuilders.delete("/delete/"+noteId)
	    			.header("Authorization", token))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    @Ignore
	    public void getAllNotes() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 1;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	
	    	mockMvc.perform(MockMvcRequestBuilders.get("/getNotes")
	    			.header("Authorization", token))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    @Ignore
	    public void collaborate() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 1;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(MockMvcRequestBuilders.post("/collaborate")
	    			.header("Authorization", token)
	    			.header("email", "akash@gmail.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    @Ignore
	    public void getOwner() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(MockMvcRequestBuilders.post("/getOwner")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    public void getCollabUser() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(MockMvcRequestBuilders.post("/getCollabUser")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
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
