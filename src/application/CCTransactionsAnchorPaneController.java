package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import dao.CreditCardsDAO;
import dao.TransactionsDAO;
import enums.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.CreditCard;
import models.Transaction;
import utils.SavingsAccountUtils;

public class CCTransactionsAnchorPaneController extends Controller implements Initializable {
	@FXML
	private Pane paneCards;
	@FXML
	private ComboBox<String> cbCards;
	@FXML
	private Button btnResetCardCBValue;
	@FXML
	private Label lblTotalCardLimit;
	@FXML
	private Label lblRemCardLimit;
	@FXML
	private Label lblTotCardLimitValue;
	@FXML
	private Label lblRemCardLimitValue;
	@FXML
	private TableView<Transaction> tblCards;
	@FXML
	private TableColumn<Transaction, String> tblClmTID;
	@FXML
	private TableColumn<Transaction, Long> tblClmCardNum;
	@FXML
	private TableColumn<Transaction, Date> tblClmTDate;
	@FXML
	private TableColumn<Transaction, Double> tblClmAmt;
	@FXML
	private TableColumn<Transaction, String> tblClmDueDate;
	@FXML
	private TableColumn<Transaction, String> tblClmStatus;
	
	private Map<String, Long> displayCardNumbersMapping;
	private CreditCard currentSelectedCard;

	@FXML
	public void displayCards(ActionEvent event) throws IOException {
		// TODO Autogenerated
		String selectedCardNumber = cbCards.getSelectionModel().getSelectedItem();
		if(selectedCardNumber != null) {
			lblTotalCardLimit.setVisible(true);
			lblTotCardLimitValue.setVisible(true);
			lblRemCardLimit.setVisible(true);
			lblRemCardLimitValue.setVisible(true);
			tblCards.setVisible(true);
			btnResetCardCBValue.setVisible(true);
			
			Long cardNumber = displayCardNumbersMapping.get(selectedCardNumber);
			currentSelectedCard = CreditCardsDAO.getCreditCardByCardNumber(cardNumber);
			lblTotCardLimitValue.setText(currentSelectedCard.getTotalCreditLimit() + "");
			lblRemCardLimitValue.setText(currentSelectedCard.getRemainingCreditLimit() + "");
			
			refreshState();
			TransactionsDAO.updateTransactionsPaymentStatus(user.getUserId().toString());
			List<Transaction> cardTransactions = 
					TransactionsDAO
					.getUserTransactions(user.getUserId().toString())
					.stream().filter(transaction -> 
						transaction.getTransactionType() == 
						TransactionType.CARD_TRANSACTION)
					.collect(Collectors.toList());
		        
			ObservableList<Transaction> transactions = 
		        		FXCollections.observableArrayList(cardTransactions);
					
			tblClmTID.setCellValueFactory(new PropertyValueFactory<>("TransactionId"));
			tblClmCardNum.setCellValueFactory(new PropertyValueFactory<>("CardNumber"));
			tblClmAmt.setCellValueFactory(new PropertyValueFactory<>("Amount"));
			tblClmTDate.setCellValueFactory(new PropertyValueFactory<>("CreatedAt"));
			tblClmDueDate.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
			tblClmStatus.setCellValueFactory(new PropertyValueFactory<>("PaymentStatus"));
			tblCards.setItems(transactions);
		}
		
	}
	// Event Listener on Button[#btnResetCardCBValue].onAction
	@FXML
	public void clearSelectedValueCardsCB(ActionEvent event) throws IOException {
		// TODO Autogenerated
		cbCards.getSelectionModel().clearSelection();
		cbCards.setButtonCell(new PromptButtonCell<>(cbCards.getPromptText()));
		lblTotalCardLimit.setVisible(false);
		lblTotCardLimitValue.setVisible(false);
		lblRemCardLimit.setVisible(false);
		lblRemCardLimitValue.setVisible(false);
		tblCards.setVisible(false);
		btnResetCardCBValue.setVisible(false);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshState();
		// TODO Auto-generated method stub
		//List of card numbers of current user
		displayCardNumbersMapping = new HashMap<>();
		List<String> cardNumbers = new ArrayList<>();
		CreditCard userCreditCard = CreditCardsDAO.getCreditCardByUserId(user.getUserId().toString());
		long cardNumber = userCreditCard.getCardNumber();
		String displayCardNumber = SavingsAccountUtils.getLastFourDigitsOf(cardNumber);
		displayCardNumbersMapping.put(displayCardNumber, cardNumber);
		cardNumbers.add(displayCardNumber);	
		
		ObservableList<String> cardNumbersList = 
				FXCollections
				.observableArrayList(cardNumbers);
		
		//initializing values of account transactions
		cbCards.setItems(cardNumbersList);
		cbCards.setStyle("-fx-font-size: 20px;");
		
		//initializing values of card transactions
		cbCards.getSelectionModel().clearSelection();
		lblTotalCardLimit.setVisible(false);
		lblTotCardLimitValue.setVisible(false);
		lblRemCardLimit.setVisible(false);
		lblRemCardLimitValue.setVisible(false);
		tblCards.setVisible(false);
		btnResetCardCBValue.setVisible(false);
	}
}