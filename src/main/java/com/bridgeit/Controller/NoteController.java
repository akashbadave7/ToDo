package com.bridgeit.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.Service.NoteService;
import com.bridgeit.Service.UserService;
import com.bridgeit.Token.VerifyToken;
import com.bridgeit.Util.GetLinkInformation;
import com.bridgeit.Util.pojo.UrlData;
import com.bridgeit.model.Label;
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
	public ResponseEntity addNote(@RequestBody NoteBean note,HttpSession session,HttpServletRequest request) {
		
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
			 note.setTrash(false);
			 System.out.println("notecolor::"+note.getColor());
			 id = noteService.saveNote(note);
			 
		 }else{
			 return new ResponseEntity(HttpStatus.BAD_REQUEST);
			 /*responseMessage.setResponseMessage("User does not exist");
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);*/
		 }
		 if(id!=0){
			 return new ResponseEntity(HttpStatus.OK);
			 /*responseMessage.setResponseMessage("Note successfully added");
			 return ResponseEntity.status(HttpStatus.OK).body(responseMessage);*/
		 }else{
			 return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			 /*responseMessage.setResponseMessage("note could not be added");
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);*/
		 }
	}
	
	/*----------------------------------Update note-------------------------------*/
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<ResponseMessage> updateNote(@RequestBody NoteBean note,HttpSession session,HttpServletRequest request,HttpServletResponse response)
	{
		/* UserBean user = (UserBean) session.getAttribute(session.getId());*/
		
		ResponseMessage responseMessage = new ResponseMessage();
		 String token = request.getHeader("Authorization");
		 System.out.println(token);
		
		 UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		 if(user!=null){
			 /*if(user.getId()==note.getNoteId()){*/

				 Date updatedDate = new Date();
			/*	 String url = String.valueOf(request.getRequestURL());
	 			 url = url.substring(0,url.lastIndexOf("/"))+"/update/";
	 			 System.out.println(url);*/
				 note.setLastUpdated(updatedDate);
				 NoteBean oldNote = noteService.getNoteById(note.getNoteId());
				 note.setUser(oldNote.getUser());
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
		System.out.println("Inside delete");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		NoteBean oldNote = noteService.getNoteById(id);
		if(user!=null)
		{
			if(user.getId()==oldNote.getUser().getId()){
				noteService.deleteNote(oldNote);
			}else
			{
				responseMessage.setResponseMessage(" deleted unsuccessfull");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
			}
		}else {
			responseMessage.setResponseMessage("User Not logged in");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		responseMessage.setResponseMessage("Deletion successfull.");
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	/*----------------------------------getAllNotes note-------------------------------*/
	
	@RequestMapping(value="/getNotes",method=RequestMethod.GET)
	public ResponseEntity<Object> getAllNotes(HttpSession session,HttpServletRequest request)
	{
		
		String token = request.getHeader("Authorization");
		System.out.println(token);
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		List<NoteBean> notes=null; 
		if(user!=null)
		{
			 notes = noteService.getAllNotes(user);
			 List<NoteBean> collborated =noteService.getCollboratedNotes(user.getId());
			 notes.addAll(collborated);
		}
		else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not logged in");
		}
		return ResponseEntity.ok(notes);
	}
	
	/*----------------------------------Collaborate note-------------------------------*/
	
	@RequestMapping(value="/collaborate",method=RequestMethod.POST)
	public ResponseEntity<Object> collaborate(@RequestBody NoteBean note,HttpServletRequest request)
	{
		System.out.println("inside collaborator");
		ResponseMessage responseMessage = new ResponseMessage();
		String token = request.getHeader("Authorization");
		UserBean currentUser = userService.getUserById(verifyToken.parseJWT(token));
		if(currentUser!=null){
			String cEmail = request.getHeader("email");
			System.out.println("collaborated user "+cEmail);
			UserBean collaboratedUser = userService.getUserByEmail(cEmail);
			if(collaboratedUser!=null)
			{
				System.out.println(collaboratedUser.getMobilenumber());
				NoteBean oldNote = noteService.getNoteById(note.getNoteId());
				oldNote.getCollaborator().add(collaboratedUser);
				noteService.updateNote(oldNote);
				
			}else{
				responseMessage.setResponseMessage("Enter Valid Email");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
			}
			
		}else
		{
			responseMessage.setResponseMessage("User Not logged in");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		return null;
		
	}
	
	
	/*----------------------------------GET OWNER-------------------------------*/

	@RequestMapping(value="/getOwner",method=RequestMethod.POST)
	public ResponseEntity<Object> getOwner(@RequestBody NoteBean note)
	{
		
		NoteBean ownerNote = noteService.getNoteById(note.getNoteId());
		return ResponseEntity.ok(ownerNote.getUser());
		
	}
	
	@RequestMapping(value="/getCollabUser",method=RequestMethod.POST)
	public ResponseEntity<Object> getCollabUser(@RequestBody NoteBean note)
	{
		NoteBean collabNoteUsers = noteService.getNoteById(note.getNoteId());
		return ResponseEntity.ok(collabNoteUsers.getCollaborator());
		
	}
	
	/*----------------------------------REMOVE COLLABORATE-------------------------------*/
	
	@RequestMapping(value="/removeCollaborator",method=RequestMethod.POST)
	public ResponseEntity<Void> removeCollaborator(@RequestBody NoteBean note,HttpServletRequest request)
	{
		
		NoteBean oldNote = noteService.getNoteById(note.getNoteId());
		String collabeUserEmail= request.getHeader("email");
		UserBean user=userService.getUserByEmail(collabeUserEmail);
		if(user!=null)
		{
			System.out.println("Collabeuser "+user.getEmail());
			System.out.println("Before remove ="+oldNote.getCollaborator());
			oldNote.getCollaborator().remove(user);
			System.out.println("After Remove ="+oldNote.getCollaborator());
			noteService.updateNote(oldNote);
			
		}else{
			
		}
		return null;
		
	}
	
	/*----------------------------------ADD LABEL-------------------------------*/
	@RequestMapping(value = "/addlabel", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> addLabel(@RequestBody Label label,HttpServletRequest request)
	{
		String token = request.getHeader("Authorization");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		System.out.println(label.getName());
		ResponseMessage responseMessage = new ResponseMessage();
		if(user!=null){
			
			int i=noteService.addLabel(label, user.getId());
			System.out.println("Label successfully added "+ i);
			if(i!=0){
				 responseMessage.setResponseMessage("Label successfully added");
				 return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
			 }else{
				 responseMessage.setResponseMessage("Label could not be added");
				 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
			 }
		}else{
			 responseMessage.setResponseMessage("User Not exits");
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		
	}
	
	/*----------------------------------GET LABEL-------------------------------*/

	@RequestMapping(value = "/getAllLabel", method = RequestMethod.POST)
	public ResponseEntity<Object> getAllLabel(HttpServletRequest request)
	{
		String token = request.getHeader("Authorization");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		Set<Label> labels=null;
		if(user!=null)
		{
			 labels= user.getLabels();
		}
		else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not logged in");
		}
		return ResponseEntity.ok(labels);
	
	}
	
	/*----------------------------------DELETE LABEL-------------------------------*/

	
	@RequestMapping(value="/deleteLabel",method=RequestMethod.POST)
	public ResponseEntity<Object> detelelabel (@RequestBody Label label,HttpServletRequest request)
	{
		System.out.println("Inside delete label");
		String token = request.getHeader("Authorization");
		UserBean user = userService.getUserById(verifyToken.parseJWT(token));
		ResponseMessage responseMessage = new ResponseMessage();
		if(user!=null){
			noteService.deleteLabel(label, user);
		}else{
			responseMessage.setResponseMessage("User Not exits");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
		}
		return null;
		
	}
	
	
	/*----------------------------------URL INFO-------------------------------*/

	@RequestMapping(value = "/geturl", method = RequestMethod.POST)
	public ResponseEntity<?> getUrlData(HttpServletRequest request){
		String urlmap=request.getHeader("url");
		GetLinkInformation link=new GetLinkInformation();
		UrlData urlData=null;
		try {
			urlData = link.getMetaData(urlmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(urlData);
	}
	
	
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value=Exception.class)
	public String handlerException(Exception e){
		e.printStackTrace();
		System.out.println();
		return "Exception"+e;
	}
	
	
}
