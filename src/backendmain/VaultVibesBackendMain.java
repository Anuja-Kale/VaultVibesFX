package backendmain;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.DatabaseConnectionFactory;
import models.CreditCard;
import models.SavingsAccount;
import models.User;
import services.UserService;

public class VaultVibesBackendMain {
	
	public static void backendMain() {
		Map<String, Object> newUserData = new HashMap<>();
		newUserData.put("name", "varsha");
		newUserData.put("email", "varshakuruva6@gmail.com");
		newUserData.put("password", "Vault@11");
		newUserData.put("phone", "6179716426");
		
		User user = UserService.findOrCreateUser(newUserData);
		
		try {
			
		double amount = 10000;
		
		SavingsAccount account = user.getAccounts().get(0);
		String accountId = account.getAccountId().toString();
		double accountBalance = account.getAccountBalance();
		
		CreditCard creditCard = user.getCreditCard();
		String cardId = creditCard.getCreditCardId().toString();
		double remainingCreditLimit = creditCard.getRemainingCreditLimit();
		double totalCreditLimit = creditCard.getTotalCreditLimit();
		
		double payableAmount = totalCreditLimit - remainingCreditLimit;
		
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName())
			.log(Level.SEVERE, null, exception);
		}
	}
}
