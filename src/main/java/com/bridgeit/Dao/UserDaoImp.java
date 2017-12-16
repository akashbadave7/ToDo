package com.bridgeit.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
			BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
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
			System.out.println("userDaoimpl"+id);
		Session session = factory.openSession();
		UserBean user = session.get(UserBean.class, id);
		session.close();
		return user;
	}

	@Override
	public UserBean getUserByEmail(String email) {
		Session session = factory.openSession();
		UserBean user=null;
		try {
		
		Query<UserBean> query = session.createQuery("from UserBean where email=:email");
		query.setParameter("email", email);
		List<UserBean> list = query.list();
		if(list.isEmpty())
		{
			return null;
		} else {
			user = list.get(0);
		}
		
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return user;
	/*	 CriteriaBuilder builder = session.getCriteriaBuilder();
	      CriteriaQuery<UserBean> criteriaQuery = builder.createQuery(UserBean.class);
	      Root<UserBean> root = criteriaQuery.from(UserBean.class);
	      criteriaQuery.select(root);
	      Query<UserBean> query = session.createQuery(criteriaQuery);
	      List<UserBean> list = query.list();
	      for(UserBean userDetails:list) {
	    	  if(userDetails.getEmail().equals(email) || userDetails.getMobilenumber().equals(mobilenumber)) {
	    		    session.close();
	    		  return userDetails;
	    	  }
	      } */
		
	}

	@Override
	public boolean isUserExits(String email,String mobilenumebr) {
		
		Session session = factory.openSession();
		List<UserBean> list=null;
		try {
				Query<UserBean> query = session.createQuery("from UserBean where email=:email or mobilenumber=:mobilenumber");
				query.setParameter("email", email);
				query.setParameter("mobilenumber", mobilenumebr);
				list = query.list();
		}catch (HibernateException e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		if(list.isEmpty())
			return true;
		else
			return false;
	}

	@Override
	public List<String> getUsers(String keyword) {
		Session session = factory.openSession();
		
		List<UserBean> users= null;
		try {
			Criteria criteria = session.createCriteria(UserBean.class);
			
			criteria.add(Restrictions.ilike("email", keyword));
			criteria.setProjection(Projections.property("email"));
			List<String> emails = criteria.list();
			return emails;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public List<UserBean> getUserList() {
		Session session=factory.openSession();

       Criteria criteria = session.createCriteria(UserBean.class);
       criteria.setProjection(Projections.property("email"));
        List<UserBean> userList=criteria.list();
		return userList;
	}
	
}
