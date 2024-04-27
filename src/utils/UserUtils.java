package utils;

import dao.UsersDAO;
import models.User;
import notifications.EmailService;

public class UserUtils {

	public static boolean isEmailPasswordValid(String email, String password) {
		boolean userExists = UsersDAO.userExistsByEmail(email);
		if (!userExists) {
			return false;
		}
		User user = UsersDAO.getUserByEmail(email);
		String hashedPasswordFromDB = user.getPassword();
		boolean passwordHashMached = PasswordUtils.checkPassword(password, hashedPasswordFromDB);
		if (passwordHashMached)
			return true;
		return false;
	}

	public static boolean sendEmailVerificationOTP(String email, int generatedOTP) {

		String subject = "Vault Vibes Email Verification";
		String message = "Enter this verifiction code to verify your email\n" + "Verification Code : "+generatedOTP;
		return EmailService.sendEmail(email, subject, message);
	}
}
