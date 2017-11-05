package com.bridgeit.Service;

import com.bridgeit.Dao.UserDao;
import com.bridgeit.model.UserBean;

public class UserServiceImp implements UserService{

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
	public UserBean getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public boolean updateUser(UserBean user) {
		return userDao.updateUser(user);
	}

}
