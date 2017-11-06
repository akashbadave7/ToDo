package com.bridgeit.Validation;

import com.bridgeit.model.UserBean;

public class Validation 
{
	public boolean signUpValidator(UserBean user) 
	{
		boolean isValid=true;
		String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,3}$";
		String  mobilePattern="^((\\+)?(\\d{2}[-]))?(\\d{10})?$";
		String namePattern="^[a-zA-Z\\s]*$";
		if(!user.getName().matches(namePattern)||user.getName()==null)
		{
			isValid=false;
		}
		else if(!user.getEmail().matches(emailPattern)||user.getEmail()==null)
		{
			isValid=false;
		}
		else if(!user.getMobilenumber().matches(mobilePattern)||user.getMobilenumber()==null)
		{
			isValid=false;
		}
		else if(user.getPassword().length()<8||user.getPassword()==null)
		{
			isValid=false;
		}
		else
		{
			isValid=true;
		}
		return isValid;
	}
}