package com.bridgeit.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.Dao.NoteDao;
import com.bridgeit.model.NoteBean;
import com.bridgeit.model.UserBean;

public class NoteServiceImp implements NoteService{

	@Autowired
	NoteDao noteDao;
	@Override
	public int saveNote(NoteBean note) {
		return noteDao.saveNote(note);
	}

	@Override
	public boolean updateNote(NoteBean note) {
		// TODO Auto-generated method stub
		return noteDao.updateNote(note);
	}

	@Override
	public boolean deleteNote(NoteBean note) {
		// TODO Auto-generated method stub
		return noteDao.deleteNote(note);
	}

	@Override
	public List<NoteBean> getAllNotes(UserBean user) {
		// TODO Auto-generated method stub
		return noteDao.getAllNotes(user);
	}

	@Override
	public NoteBean getNoteById(int noteId) {
		// TODO Auto-generated method stub
		return noteDao.getNoteById(noteId);
	}

}
