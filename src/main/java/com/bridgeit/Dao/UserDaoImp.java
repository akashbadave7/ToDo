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

import com.bridgeit.model.UserBean;


public class UserDaoImp implements UserDao{
	
	@Autowired
	SessionFactory factory;
	public SessionFactory getFactory() {
		return factory;
	}
	
	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}
	@Override
	public int addUser(UserBean user) {
		
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		int i=0;
		try
		{	
			i = (int) session.save(user);
			transaction.commit();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
		return i;
	}

	@Override
	public boolean updateUser(UserBean user) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(user);
			transaction.commit();
		}
		catch(HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		}finally {
			session.close();
		}
		return true;
	}

	@Override
	public boolean deleteUser(UserBean user) {
		
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(user);
			transaction.commit();
		}
		catch(HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		}finally {
			session.close();
		}
		return true;
	}

	@Override
	public UserBean getUserById(int id) {
			
		Session session = factory.openSession();
		UserBean user = session.get(UserBean.class, id);
		session.close();
		return user;
		
		
	}

	@Override
	public UserBean getUserByEmail(UserBean user) {
		Session session = factory.openSession();
		 CriteriaBuilder cb = session.getCriteriaBuilder();
	      CriteriaQuery<UserBean> cq = cb.createQuery(UserBean.class);
	      Root<UserBean> root = cq.from(UserBean.class);
	      cq.select(root);
	      Query<UserBean> query = session.createQuery(cq);
	      List<UserBean> list = query.list();
		
	      for(UserBean userDetails:list) {
	    	  if(userDetails.getEmail().equals(user.getEmail()) || userDetails.getMobilenumber().equals(user.getMobilenumber())) {
	    		  return userDetails;
	    	  }
	      }
		return null;
	}

	@Override
	public boolean isUserExits(UserBean user) {
		String email= user.getEmail();
		UserBean isUserExit = getUserByEmail(user);
		if(isUserExit==null)
			return true;
		else
			return false;
	}
	
}
