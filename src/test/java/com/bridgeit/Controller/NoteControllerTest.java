package com.bridgeit.Controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bridgeit.Service.NoteService;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.TokenGenerator;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.model.Label;
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
	   // @Ignore
	    public void addNote() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setTitle("Note 4");
	    	note.setBody("Note 4");
	    	mockMvc.perform(post("/addNote")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void update() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	note.setTitle("Note 2 update");
	    	note.setBody("Note 2 update");
	    	mockMvc.perform(post("/update")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }


	    @Test
	    //@Ignore
	    public void deleteNoteById() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	int noteId=3;
	    	
	    	mockMvc.perform(delete("/delete/"+noteId)
	    			.header("Authorization", token))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void getAllNotes() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	
	    	mockMvc.perform(get("/getNotes")
	    			.header("Authorization", token))
	    			.andExpect(status().isOk());
	    	
	  /*  	mockMvc.perform(get("/getNotes")
			    	.header("Authorization", token))
		            .andExpect(status().isOk())
		            .andExpect(view().name("todo/list"))
		            .andExpect(model().attribute("todo", hasSize(8)));*/
	    }
	    
	    
	    
	    @Test
	    //@Ignore
	    public void collaborate() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(post("/collaborate")
	    			.header("Authorization", token)
	    			.header("email","rollingcola@yahoo.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void getOwner() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(post("/getOwner")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void getCollabUser() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	mockMvc.perform(post("/getCollabUser")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    
	    @Test
	    //@Ignore
	    public void removeCollaborator() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(2);
	    	mockMvc.perform(post("/removeCollaborator")
	    			.header("email","test@gmail.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void addlabel() throws Exception{
	    	
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	Label label = new Label();
	    	label.setName("junit test");
	    	mockMvc.perform(post("/addlabel")
	    			.header("Authorization",token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(label)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void getAllLabel() throws Exception{
	    	
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    
	    	mockMvc.perform(post("/getAllLabel")
	    			.header("Authorization",token)
	    			.contentType(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void deleteLabel() throws Exception{
	    	
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	Label label = new Label();
	    	label.setLabelId(1);
	    	mockMvc.perform(post("/deleteLabel")
	    			.header("Authorization",token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(label)))
	    			.andExpect(status().isOk());
	    }
	    
	    
	    
	    @Test
	    //@Ignore
	    public void geturl() throws Exception{
	    	
	    	mockMvc.perform(post("/geturl")
	    			.header("url","https://www.ndtv.com/india-news/gujarat-assembly-election-result-2017-live-updates-will-it-be-pm-narendra-modis-bjp-or-rahul-gandhis-1789005")
	    			.contentType(MediaType.APPLICATION_JSON))
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
