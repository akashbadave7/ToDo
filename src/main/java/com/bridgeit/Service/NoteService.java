package com.bridgeit.Service;

import java.util.List;
import java.util.Set;

import com.bridgeit.model.Label;
import com.bridgeit.model.NoteBean;
import com.bridgeit.model.UserBean;

public interface NoteService {

	public int saveNote(NoteBean note);
	
	public boolean updateNote(NoteBean note);
	
	public boolean deleteNote(NoteBean note);
	
	public List<NoteBean> getAllNotes(UserBean user);
	
	public NoteBean getNoteById(int noteId);

	public List<NoteBean> getCollboratedNotes(int id);

	public void removeCollabeUser(NoteBean oldNote, UserBean user);
	
	public int addLabel(Label label,int userId);
	
	public boolean deleteLabel(int id,int userId);
	
	public boolean updateLabel(Label label,int userId);
	
	public Set<Label> getAllLabel(int userId);
	
	
}
