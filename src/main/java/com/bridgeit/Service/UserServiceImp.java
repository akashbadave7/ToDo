package com.bridgeit.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgeit.Dao.UserDao;
import com.bridgeit.model.UserBean;

public class UserServiceImp implements UserService{

	@Autowired
	UserDao userDao;
	@Override
	public int saveUserData(UserBean user) {
		return userDao.addUser(user);
	}

	@Override
	public UserBean getUserById(int id) {
		return userDao.getUserById(id);
	}

	@Override
	public UserBean getUserByEmail(UserBean user) {
		return userDao.getUserByEmail(user);
	}

	@Override
	public boolean updateUser(UserBean user) {
		return userDao.updateUser(user);
	}


	@Override
	public boolean isUserExits(UserBean user) {
		return userDao.isUserExits(user);
	}


}
