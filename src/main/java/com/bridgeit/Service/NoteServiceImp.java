package com.bridgeit.Service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.Dao.NoteDao;
import com.bridgeit.model.Label;
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

	@Override
	public List<NoteBean> getCollboratedNotes(int id) {
		return noteDao.getCollboratedNotes(id);	
	}

	@Override
	public void removeCollabeUser(NoteBean oldNote, UserBean user) {
		// TODO Auto-generated method stub
		noteDao.removeCollabeUser(oldNote,user);
	}

	@Override
	public int addLabel(Label label, int userId) {
		// TODO Auto-generated method stub
		
		return noteDao.addLabel(label,userId);
	}

	@Override
	public boolean deleteLabel(int id, int userId) {
		// TODO Auto-generated method stub
		return noteDao.deleteLabel(id,userId);
	}



	@Override
	public boolean updateLabel(Label label, int userId) {
		// TODO Auto-generated method stub
		return noteDao.updateLabel(label,userId);
	}

	@Override
	public Set<Label> getAllLabel(int userId) {
		// TODO Auto-generated method stub
		return noteDao.getAllLabel(userId);
	}

	

}
