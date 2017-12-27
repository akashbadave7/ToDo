package com.bridgeit.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
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
	    private TokenGenerator tokenService;

	    
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
	    
	 /*=======================================ADD NOTE TEST CASES============================*/   
	    
	    /**
	     * @throws Exception
	     * Test case for adding note with valid user
	     */
	    @Test
	    //@Ignore
	    public void addNote() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setTitle("Note 9");
	    	note.setBody("Note 9");
	    	mockMvc.perform(post("/addNote")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andDo(MockMvcResultHandlers.print())
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("Note successfully added")))
	    			.andExpect(status().isOk());
	    }
	    
	    
	    /**
	     * @throws Exception
	     * Test case for adding note with invalid user
	     */
	    @Test
	    //@Ignore
	    public void addNoteInvalidUser() throws Exception{
	    	String email="akash7@gmail.com";
	    	int id = 20;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setTitle("Note 5");
	    	note.setBody("Note 5");
	    	mockMvc.perform(post("/addNote")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andDo(MockMvcResultHandlers.print())
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("User does not exist")))
	    			.andExpect(status().isUnauthorized());
	    }
	    
		 /*=======================================UPDATE NOTE TEST CASES============================*/   

	    /**
	     * @throws Exception
	     * Test Case for update note with valid user
	     */
	    @Test
	    //@Ignore
	    public void update() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(2);
	    	note.setTitle("Note 2");
	    	note.setBody("Note 2");
	    	mockMvc.perform(post("/update")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
			    	.andDo(MockMvcResultHandlers.print())
					.andExpect(jsonPath("$.responseMessage", Matchers.is("Updated Successfully")))
	    			.andExpect(status().isOk());
	    }
	    
	    /**
	     * @throws Exception
	     * Test Case for update note failed
	     */
	    @Test
	   //@Ignore
	    public void updateFailed() throws Exception{
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
	    			.andDo(MockMvcResultHandlers.print())
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("Update failed")))
	    			.andExpect(status().isInternalServerError());
	    }
	    
	    /**
	     * @throws Exception
	     * Test Case for update note with invalid user
	     */
	    @Test
	    //@Ignore
	    public void updateWithInvalidUser() throws Exception{
	    	String email="akash.7@gmail.com";
	    	int id = 20;
	    	String token=tokenService.createJWT(id, email);
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(1);
	    	note.setTitle("Note 2 update");
	    	note.setBody("Note 2 update");
	    	mockMvc.perform(post("/update")
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
			    	.andDo(MockMvcResultHandlers.print())
					.andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
	    			.andExpect(status().isUnauthorized());
	    }
	    
	   
		 /*=======================================DELETE NOTE TEST CASES============================*/   

	    /**
	     * @throws Exception
	     * Test Case for delete note with invalid user
	     */
	    @Test
	    //@Ignore
	    public void deleteWithInvalidUser() throws Exception{
	    	String email="akash.7@gmail.com";
	    	int id = 20;
	    	String token=tokenService.createJWT(id, email);
	    	int noteId=1;
	    	
	    	mockMvc.perform(delete("/delete/"+noteId)
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON))
			    	.andDo(MockMvcResultHandlers.print())
					.andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
	    			.andExpect(status().isUnauthorized());
	    }
	    
	    
	    /**
	     * @throws Exception
	     * Test Case for delete note with valid user
	     */
	    @Test
	   //@Ignore
	    public void deleteNoteById() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	int noteId=3;
	    	
	    	mockMvc.perform(delete("/delete/"+noteId)
	    			.header("Authorization", token)
	    			.contentType(MediaType.APPLICATION_JSON))
	    			.andDo(MockMvcResultHandlers.print())
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("Deletion successfull.")))
	    			.andExpect(status().isOk());
	    }
	    
		 /*=======================================GET ALL NOTE TEST CASES============================*/   

	    /**
	     * @throws Exception
	     * Test Case to get all note with valid user
	     */
	    @Test
	    //@Ignore
	    public void getAllNotes() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	
	     mockMvc.perform(get("/getNotes")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.header("Authorization", token))
	    			.andExpect(status().isOk())
	    			 .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
	    			 .andDo(MockMvcResultHandlers.print())
	    			 .andExpect((jsonPath("$", Matchers.hasSize(2))));
	    }
	    
    	/**
    	 * @throws Exception
    	 * Test case for get all notes with invalid user
    	 */
    	@Test
	    //@Ignore
	    public void getAllNotesWithInvalidUser() throws Exception{
	    	String email="akash7@gmail.com";
	    	int id = 20;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	mockMvc.perform(get("/getNotes")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.header("Authorization", token))
	    			.andExpect(status().isUnauthorized())
	    			 .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
	    			 .andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
	    			 .andDo(MockMvcResultHandlers.print());
	    }
	    
	/*=======================================Collaborate NOTE TEST CASES============================*/   

	    /**
	     * @throws Exception
	     * Test case to collaborate note  with invalid user
	     */
	    @Test
	    //@Ignore
	    public void collaborateIvalidUser() throws Exception{
	    	String email="akash78@gmail.com";
	    	int id = 20;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(4);
	    	mockMvc.perform(post("/collaborate")
	    			.header("Authorization", token)
	    			.header("email","rollingcola@yahoo.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isUnauthorized())
	    			.andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("User Not logged in")))
	    			.andDo(MockMvcResultHandlers.print());
	    }
	    
	    
	    /**
	     * @throws Exception
	     * Test case to collaborate user with invalid collabortor email
	     */
	    @Test
	    //@Ignore
	    public void collaborateIvalidCollabUser() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(4);
	    	mockMvc.perform(post("/collaborate")
	    			.header("Authorization", token)
	    			.header("email","sadasdjkashdasd@yahoo.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isBadRequest())
	    			.andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("Enter Valid Email")))
	    			.andDo(MockMvcResultHandlers.print());
	    }
	    
	    
	    /**
	     * @throws Exception
	     * Test case to collaborate user with valid email
	     */
	    @Test
	    //@Ignore
	    public void collaborateUser() throws Exception{
	    	String email="akash.badave7@gmail.com";
	    	int id = 4;
	    	String token=tokenService.createJWT(id, email);
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(4);
	    	mockMvc.perform(post("/collaborate")
	    			.header("Authorization", token)
	    			.header("email","akash@gmail.com")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk())
	    			.andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
	    			.andExpect(jsonPath("$.responseMessage", Matchers.is("Successfully collaborated")))
	    			.andDo(MockMvcResultHandlers.print());
	    }
	    
	    @Test
	    //@Ignore
	    public void getOwner() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(4);
	    	mockMvc.perform(post("/getOwner")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(asJsonString(note)))
	    			.andExpect(status().isOk());
	    }
	    
	    @Test
	    //@Ignore
	    public void getCollabUser() throws Exception{
	    	
	    	NoteBean note = new NoteBean();
	    	note.setNoteId(4);
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
	    			.header("email","akash@gmail.com")
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
	    	label.setName("Label 3");
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
	    	label.setLabelId(8);
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
