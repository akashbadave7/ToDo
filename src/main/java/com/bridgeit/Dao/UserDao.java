package com.bridgeit.Dao;

import java.util.List;

import com.bridgeit.model.UserBean;

public interface UserDao {

	int addUser(UserBean user);
	
	boolean updateUser(UserBean user);
	
	boolean deleteUser(UserBean user);
	
	
	UserBean getUserById(int id);
	
	UserBean getUserByEmail(String email);
	
	boolean isUserExits(String email,String mobilenumber);
	
	
	List<UserBean> getUserList();
}
