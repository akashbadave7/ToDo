package com.bridgeit.Dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.model.NoteBean;
import com.bridgeit.model.UserBean;

public class NoteDaoImp implements NoteDao{
	
	@Autowired
	SessionFactory factory;
	
	public SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	/*-----------------------------Save Note------------------------------*/
	@Override
	public int saveNote(NoteBean note) {
		Session session  = factory.openSession();
		Transaction transaction=null;
		int id=0;
		try
		{
			transaction = session.beginTransaction();
			id= (int) session.save(note);
			transaction.commit();
		}catch(HibernateException e){
			if(transaction!=null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return id;
	}
	/*-----------------------------Update Note------------------------------*/

	@Override
	public boolean updateNote(NoteBean note) {
		Session session  = factory.openSession();
		Transaction transaction=null;
		try
		{
			transaction = session.beginTransaction();
			session.saveOrUpdate(note);
			transaction.commit();
		}catch(HibernateException e){
			if(transaction!=null) {
				transaction.rollback();
				return false;
			}
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean deleteNote(NoteBean note) {
		Session session  = factory.openSession();
		Transaction transaction=null;
		transaction=session.beginTransaction();
		try
		{
			transaction = session.beginTransaction();
			session.delete(note);
			transaction.commit();
		}catch(HibernateException e){
			if(transaction!=null) {
				transaction.rollback();
				return false;
			}
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<NoteBean> getAllNotes(UserBean user) {
		Session session = factory.openSession();
		 CriteriaBuilder builder = session.getCriteriaBuilder();
	      CriteriaQuery<UserBean> criteriaQuery = builder.createQuery(UserBean.class);
	      Root<UserBean> root = criteriaQuery.from(UserBean.class);
	      criteriaQuery.select(root);
	      Query<UserBean> query = session.createQuery(criteriaQuery);
	      List<UserBean> list = query.list();
	      List<NoteBean> note=null;
	      for(UserBean userDetails:list) {
	    	  note = userDetails.getNotes();
	    	  }
		return note;
	 }


	@Override
	public NoteBean getNoteById(int noteId) {
		Session session  = factory.openSession();
		NoteBean note = session.get(NoteBean.class, noteId);
		return note;
	}
	
	

}
