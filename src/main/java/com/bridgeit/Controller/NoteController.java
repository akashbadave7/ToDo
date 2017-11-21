package com.bridgeit.Controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.Service.NoteService;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.model.NoteBean;
import com.bridgeit.model.ResponseMessage;
import com.bridgeit.model.UserBean;

@RestController
public class NoteController {

	@Autowired
	private NoteService noteService;
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyToken verifyToken;

	
	
	/*----------------------------------Adding note-------------------------------*/

	@RequestMapping(value="/addNote",method=RequestMethod.POST)
	public ResponseEntity<ResponseMessage> addNote(@RequestBody NoteBean note,HttpSession session,HttpServletRequest request) {
		
		 /*UserBean user = (UserBean) session.getAttribute(session.getId());*/
		ResponseMessage responseMessage = new ResponseMessage();
		String token = request.getHeader("Authorization");
		System.out.println(token);
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		 int id=0;
		 if (user!=null) {
			 note.setUser(user);
			 Date createdDate  = new Date();
			 note.setCreateDate(createdDate);
			 note.setLastUpdated(createdDate);
			 id = noteService.saveNote(note);
			 
		 }else{
			 responseMessage.setResponseMessage("User does not exist");
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		 }
		 if(id!=0){
			 responseMessage.setResponseMessage("Note successfully added");
			 return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
		 }else{
			 responseMessage.setResponseMessage("note could not be added");
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
		 }
	}
	
	/*----------------------------------Update note-------------------------------*/
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<ResponseMessage> updateNote(@RequestBody NoteBean note,HttpSession session,HttpServletRequest request,HttpServletResponse response)
	{
		/* UserBean user = (UserBean) session.getAttribute(session.getId());*/
		
		ResponseMessage responseMessage = new ResponseMessage();
		 String token = request.getHeader("Authorization");
		 UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		 if(user!=null){
			 /*if(user.getId()==note.getNoteId()){*/

				 Date updatedDate = new Date();
			/*	 String url = String.valueOf(request.getRequestURL());
	 			 url = url.substring(0,url.lastIndexOf("/"))+"/update/";
	 			 System.out.println(url);*/
				 note.setLastUpdated(updatedDate);
				 note.setUser(user);
				 if(noteService.updateNote(note))
				 {
					 responseMessage.setResponseMessage("Updated Successfully");
					 return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
				 }else {
					 responseMessage.setResponseMessage("Update failed");
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
				 }
			 /*} else {
				 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not modify");
			 }*/
		 }else{
			 responseMessage.setResponseMessage("User Not logged in");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
		 }
	}
	
	/*----------------------------------Delete note-------------------------------*/

	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<ResponseMessage> delete (@PathVariable("id") int id, HttpSession session,HttpServletRequest request)
	{
		//UserBean user = (UserBean) session.getAttribute(session.getId());
		ResponseMessage responseMessage = new ResponseMessage();
		String token = request.getHeader("Authorization");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		if(user!=null)
		{
			/*if(user.getId()==id)
			{*/
				List<NoteBean> list = noteService.getAllNotes(user);
				for (int i=0;i<list.size();i++)
				{
					NoteBean note = list.get(i);
					if(note.getNoteId()==id) {
						noteService.deleteNote(note);
						responseMessage.setResponseMessage("Successfully deleted");
						return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
					}
				}
			/*}else
			{
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not delete");
			}*/
		}else {
			responseMessage.setResponseMessage("User Not logged in");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		responseMessage.setResponseMessage("Deletion not possible.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
	}
	
	/*----------------------------------getAllNotes note-------------------------------*/
	
	@RequestMapping(value="/getNotes",method=RequestMethod.GET)
	public ResponseEntity<Object> getAllNotes(HttpSession session,HttpServletRequest request)
	{
		ResponseMessage responseMessage = new ResponseMessage();
		//UserBean user = (UserBean) session.getAttribute(session.getId());
		String token = request.getHeader("Authorization");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		List<NoteBean> notes=null;
		if(user!=null)
		{
			 notes = noteService.getAllNotes(user);
		}
		else{
			responseMessage.setResponseMessage("User Not Logged In");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		return ResponseEntity.ok(notes);
	}
	
}
