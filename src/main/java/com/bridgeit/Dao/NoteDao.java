package com.bridgeit.Dao;

import java.util.Set;

import com.bridgeit.model.NoteBean;

public interface NoteDao {

	public int saveNote(NoteBean note);
	
	public boolean updateNote(NoteBean note);
	
	public boolean deleteNote(NoteBean note);
	
	public Set<NoteBean> getNotes(int id);
	
	public NoteBean getNoteById(int noteId);
}
