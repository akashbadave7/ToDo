package com.bridgeit.Controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;

import com.bridgeit.Service.NoteService;
import com.bridgeit.model.NoteBean;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class})
@WebAppConfiguration
public class NoteControllerTest {

	 @InjectMocks
	    NoteController noteController;

	    @Mock
	    NoteService noteService;

	    @Mock
	    View mockView;

	    MockMvc mockMvc;

	    @Before 
	    public void setUp() throws Exception {
	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
	                .setSingleView(mockView)
	                .build();
	    }

	    @Test
	    public void testListPeopleInGroup() throws Exception {
	       /* List<NoteBean> expectedPeople = asList(new NoteBean());
	        when(noteService.getAllNotes(user)("someGroup")).thenReturn(expectedPeople);
*/
	        mockMvc.perform(get("/getNotes"))
	                .andExpect(status().isOk())
	               /* .andExpect(model().attribute("people", expectedPeople))*/
	                .andExpect(view().name("peopleList"));
	    }


}
