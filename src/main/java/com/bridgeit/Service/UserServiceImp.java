package com.bridgeit.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgeit.Dao.UserDao;
import com.bridgeit.model.UserBean;


@Service("userService")
public class UserServiceImp implements UserService{

	@Autowired
	UserDao userDao;
	@Override
	public int saveUserData(UserBean user) {
		return userDao.addUser(user);
	}

	@Override
	public UserBean getUserById(int id) {
		System.out.println("user service impl"+id);
		return userDao.getUserById(id);
	}

	@Override
	public UserBean getUserByEmail(String email) {
		System.out.println("In user implemetation:"+email);
		return userDao.getUserByEmail(email);
	}

	@Override
	public boolean updateUser(UserBean user) {
		return userDao.updateUser(user);
	}


	@Override
	public boolean isUserExits(String email,String mobilenumber) {
		return userDao.isUserExits(email,mobilenumber);
	}

	@Override
	public boolean deleteUser(UserBean user) {
		return userDao.deleteUser(user);
	}

	
	
	public List<UserBean> getUserList() {
		// TODO Auto-generated method stub
		return userDao.getUserList();
	}


}
