package com.bridgeit.Dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.model.Label;
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
		}finally {
			session.close();
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
			System.out.println("exception occured");
			e.printStackTrace();
		}finally {
			session.close();
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
			session.delete(note);
			transaction.commit();

		}catch(HibernateException e){
			if(transaction!=null) {
				transaction.rollback();
				return false;
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		return true;
	}

	@Override
	public List<NoteBean> getAllNotes(UserBean user) {
		Session session = factory.openSession();
		UserBean user1 = session.get(UserBean.class, user.getId());
		List<NoteBean> note = user1.getNotes();
		note.size();
		 /*CriteriaBuilder builder = session.getCriteriaBuilder();
	      CriteriaQuery<UserBean> criteriaQuery = builder.createQuery(UserBean.class);
	      Root<UserBean> root = criteriaQuery.from(UserBean.class);
	      criteriaQuery.select(root);
	      Query<UserBean> query = session.createQuery(criteriaQuery);
	      List<UserBean> list = query.list();
	      List<NoteBean> note=null;
	      for(UserBean userDetails:list) {
	    	  note = userDetails.getNotes();
	    	  session.close();
	    	  }*/
		return note;
	 }


	@Override
	public NoteBean getNoteById(int noteId) {
		Session session  = factory.openSession();
		NoteBean note = session.get(NoteBean.class, noteId);
		note.getCollaborator().size();
		session.close();
		return note;
	}

	@Override
	public List<NoteBean> getCollboratedNotes(int userId) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		/*NoteBean notes = session.get(NoteBean.class, userId);
		System.out.println(notes.to);*/
		
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(NoteBean.class);
		criteria.createAlias("collaborator", "c");
		criteria.add(Restrictions.eq("c.id", userId));
		@SuppressWarnings("unchecked")
		List<NoteBean> collaboratedNotes = criteria.list();
		
		session.close();
		return collaboratedNotes;
	}
		/*=========================REMOVE COLLABE USER=============*/
	@Override
	public Object removeCollabeUser(NoteBean oldNote, UserBean user) {
		return null;
	}

	/*-----------------------------ADD LABEL------------------------------*/

	@Override
	public int addLabel(Label label, int userId) {
		Session session = factory.openSession();
		Transaction transaction = null;
		int i = 0;
		try {
			transaction = session.beginTransaction();
			UserBean user = new UserBean();
			user.setId(userId);
			label.setUser(user);
			i=(int) session.save(label);
			transaction.commit();
			
		} catch (HibernateException e) {
			if(transaction!=null) {
				transaction.rollback();
			}
				e.printStackTrace();
		}finally {
			session.close();
		}
		
		return i;
	}

	/*-----------------------------DELETE LABEL------------------------------*/

	@Override
	public boolean deleteLabel(Label label, UserBean user) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(label);
			for(NoteBean note : label.getNotes())
			{
				note.getLabels().remove(label);
			}
			label.getNotes().clear();
			transaction.commit();
		} catch (HibernateException e) {
			if(transaction!=null){
				transaction.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		return false;
	}

	/*-----------------------------UPDATE LABEL------------------------------*/

	@Override
	public boolean updateLabel(Label label, int userId) {
		// TODO Auto-generated method stub
		return false;
	}

	/*-----------------------------GET ALL LABEL------------------------------*/

	@Override
	public Set<Label> getAllLabel(int userId) {
		// TODO Auto-generated method stub
		return null;
	}


}
