package services;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import dao.UsersDAO;
import models.BeneficiaryUser;
import models.CreditCard;
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class UserService {
	
	public static User findOrCreateUser(Map<String, Object> newUserData) {
		
		User newUser = new User();
		newUser.setUserId(UUID.randomUUID());
		newUser.setName((String) newUserData.get("name"));
		newUser.setEmail((String) newUserData.get("email"));
		newUser.setPassword((String) newUserData.get("password"));
		newUser.setPhone((long) Long.parseLong((String) newUserData.get("phone")));
		newUser.setAccounts(new ArrayList<SavingsAccount>());
		newUser.setCreditCard(new CreditCard());
		newUser.setTransactions(new ArrayList<Transaction>());
		newUser.setBeneficiaryUsers(new ArrayList<BeneficiaryUser>());
		
		boolean existingUser = UsersDAO.userExistsByEmail(newUser.getEmail());
		User user = null;
		if(!existingUser) {
			boolean created = UsersDAO.createNewUser(newUser);
			if(created) {
				user = UsersDAO.getUserByEmail(newUser.getEmail());
				System.out.println("Created Account for User "+newUser.getName());
				return user;
			}
			else {
				System.out.println("User Already Exists / Some Error Occurred");
				return null;
			}
		}
		else {
			user = UsersDAO.getUserByEmail(newUser.getEmail());
			return user;
		}
	}
}
