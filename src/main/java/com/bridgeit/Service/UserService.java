package com.bridgeit.Service;

import com.bridgeit.model.UserBean;

public interface UserService {


		public int saveUserData(UserBean user);
		public UserBean getUserById(int id);
		public UserBean getUserByEmail(UserBean user);
		public boolean updateUser(UserBean user);
		public boolean isUserExits(UserBean user);
}
