package com.revature.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Transaction;
import com.revature.repositories.TransactionRepository;
import com.revature.services.AccountServicesImpl;
import com.revature.services.CustomerServicesImpl;
import com.revature.services.TransactionServicesImpl;

public class Driver {
	/*
	 * Bugs:
	 * 
	 * - "pending" in the DB should be changed to "status", signifiying one of three states: pending, approved, or denied
	 * - withdrawing and depositing does not update the DB
	 * - control flow everywhere needs to be refined
	 * - confirmation for transfers: "Y/N" prints twice
	 * - print out a list of customers for employee option "6"
	 */
	private enum Menus {
		MAIN,
		CUSTOMER_MAIN
	}
	
	private static Menus currentMenu = Menus.MAIN;
	
	public static final Logger logger = LogManager.getLogger(Driver.class);
	
	public static Scanner scanner;
	private static String[] main_menu = { "1. Login", "2. Sign Up" };
	private static String[] customer_menu = { "1. Apply for a new account", "2. Withdraw money", "3. Deposit money", "4. Transfer money"};
	private static String[] employee_options = { "Approve or reject an account", "View a customer's accounts", "View transaction log" };
	
	private static void printAccounts() {
		Customer customer = CustomerServicesImpl.getInstance().getCustomer();
		if (customer == null) {
			logger.error("printAccounts: customer null");
			return;
		}
		
		printMessage("\nYou are logged in as user \"" + customer.getUsername() + "\"\n");
		if (customer.getAccounts() == null || customer.getAccounts().size() == 0) {
			printMessage("You do not have any accounts currently open.");
		} else {
			printMessage("Your accounts:");
			customer.getAccounts().entrySet().forEach((e) -> {
				printMessage("    %d: $%.2f%-20s%n", e.getKey(), e.getValue().getBalance(), e.getValue().isPending() ? "Pending" : "");
			});
			printMessage("");
		}
	}
	
	private static void printAccounts(Customer customer) {
		if (customer == null) {
			logger.error("printAccounts: customer null");
			return;
		}
		
		printMessage("\nAccounts for customer \"" + customer.getUsername() + "\":\n");
		if (customer.getAccounts() == null || customer.getAccounts().size() == 0) {
			printMessage("Customer \"%s\" does not have any accounts.%n", customer.getUsername());
		} else {
			customer.getAccounts().entrySet().forEach((e) -> {
				printMessage("    %d: $%.2f%-20s%n", e.getKey(), e.getValue().getBalance(), e.getValue().isPending());
			});
			printMessage("");
		}
	}
	
	private static void printMenu(String[] menu, String extraOption, boolean printCarrot) {
		printMessage("Please select an option:\n");
		for (String option : menu) printMessage(option);
		if (extraOption != null && !extraOption.isEmpty()) printMessage("" + (menu.length + 1) + ". " + extraOption);
		if (printCarrot) printCarrot();
	}
	
	private static void printMenu(String[] menu, String extraOption, boolean printCarrot, boolean printEmployeeOptions) {
		printMenu(menu, null, false);
		if (printEmployeeOptions) { 
			for (int i = 0; i < employee_options.length; i++) {
				printMessage("%d. %s%n", i + menu.length + 1, employee_options[i]);
			}
		}
		
		if (extraOption != null && !extraOption.isEmpty()) {
			int optionNum = menu.length + 1;
			if (printEmployeeOptions) optionNum += employee_options.length;
			printMessage("%d. %s%n", optionNum, extraOption);
		}
		if (printCarrot) printCarrot();
	}
	
	private static void printCarrot() {
		System.out.print("> ");
	}
	
	public static void printMessage(String message) {
		System.out.println(message);
	}
	
	public static void printMessage(String message, boolean addNewline) {
		if (!addNewline) System.out.print(message);
		else System.out.println(message);
	}
	
	public static void printMessage(String message, Object... args) {
		System.out.printf(message, args);
	}
	
	public static boolean getConfirmation(String message, Object... args) {
		System.out.printf(message + " Y/N: ", args);
		boolean confirmation = scanner.nextLine().equalsIgnoreCase("y");
		return confirmation;
	}
	
	/**
	 * Read and process user input for the main menu.
	 * It is assumed that the menu has already been printed with printMenu().
	 * 
	 * @return whether the main do-while loop should terminate ("quit") on the next iteration
	 */
	private static boolean handleMainMenu() {
		String username;
		switch (scanner.nextInt()) {
			case 1:		// login
				if (CustomerServicesImpl.getInstance().login(scanner)) {
					currentMenu = Menus.CUSTOMER_MAIN;
					username = CustomerServicesImpl.getInstance().getCustomer().getUsername();
					logger.info("Customer " + username + " logged in.");
					return false;
				} else {
					printMenu(main_menu, "Quit", true);
					return false;
				}			
			case 2:		// sign up
				if (CustomerServicesImpl.getInstance().signUp(scanner)) {
					currentMenu = Menus.CUSTOMER_MAIN;
					username = CustomerServicesImpl.getInstance().getCustomer().getUsername();
					logger.info("Customer " + username + " has signed up.");
					return false;
				} else {
					printMenu(main_menu, "Quit", true);
					return false;
				}				
			case 3: printMessage("Goodbye!"); return true;
			default: printMessage("Please enter a valid option."); return false;
		}
	}
	
	private static void handleCustomerMenu() {
		/*
		 * "1. Apply for a new account"
		 * "2. Withdraw money"
		 * "3. Deposit money"
		 * "4. Transfer money"
		 * "5. Logout (if customer only) / Approve or reject an account"
		 * "6. View a customer's accounts"
		 * "7. View transaction log"
		 * "8. Logout (if employee only)"
		 */
		String[] command;
		int fromID, toID;
		float amount;
		boolean success;
		Customer customer = CustomerServicesImpl.getInstance().getCustomer();
		if (customer == null) {
			logger.error("handleCustomerMenu: customer null");
			return;
		}
		
//		while (true) {
			command = scanner.nextLine().split(" ");
			switch (command[0]) {
				case "1":
					// apply for a new account
					if (command.length != 2) {
						printMessage("Please enter \"1\" followed by the amount you wish to start with.");
						break;
					}
					
					amount = Float.parseFloat(command[1]);
					AccountServicesImpl.getInstance().apply(amount);
					printMessage("Your new account with balance $%.2f has been created and is pending approval. It must be approved before it can be used.", amount);
					break;
				case "2":
					// withdraw money - "2 {account_id} {amount}"
					if (command.length != 3) {
						printMessage("Please enter \"2\" followed by the account number followed by the amount you wish to withdraw.");
						break;
					}
					
					fromID = Integer.parseInt(command[1]);
					if (!customer.getAccounts().containsKey(fromID)) {
						printMessage("You do not have an account with the id of " + fromID);
						break;
					}
					
					amount = Float.parseFloat(command[2]);
					success = AccountServicesImpl.getInstance().withdraw(fromID, amount);
					if (success) {
						printMessage("You have withdrawn $%.2f from account %d.%n%n", amount, fromID);
						logger.info(String.format("Customer %s withdrew $%.2f from account %d.", customer.getUsername(), amount, fromID));
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
					break;
				case "3":
					// deposit money - "3 {account_id} {amount}"
					if (command.length != 3) {
						printMessage("Please enter \"3\" followed by the account number, followed by the amount you wish to deposit.");
						break;
					}
					
					fromID = Integer.parseInt(command[1]);
					if (!customer.getAccounts().containsKey(fromID)) {
						printMessage("You do not have an account with the id of " + fromID);
						break;
					}
					
					amount = Float.parseFloat(command[2]);
					success = AccountServicesImpl.getInstance().deposit(fromID, amount);
					if (success) {
						printMessage("You have depositted $%.2f into account %d.%n%n", amount, fromID);
						
						logger.info(String.format("Customer %s depositted $%.2f into account %d.", customer.getUsername(), amount, fromID));
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
					break;
				case "4":
					// post money transfer - "4 {from_id} {to_id} {amount}
					if (command.length != 4) {
						System.out.println("Please enter \"4\" followed by the account number you want to transfer from, followed by the account number you want to transfer to, followed by the amount you wish to transfer.");
						break;
					}
					
					fromID = Integer.parseInt(command[1]);
					toID = Integer.parseInt(command[2]);
					if (!customer.getAccounts().containsKey(fromID)) {
						System.out.println("You do not have an account with the id of " + fromID);
						break;
					} else if (!customer.getAccounts().containsKey(toID)) {
						System.out.println("You do not have an account with the id of " + toID);
						break;
					}
					
					amount = Float.parseFloat(command[3]);
					success = AccountServicesImpl.getInstance().transfer(fromID, toID, amount);
					if (success) {
						printMessage("Transfer confirmed.");
						logger.info(String.format("Customer %s transferred $%.2f from account %d to account %d.", customer.getUsername(), amount, fromID, toID));
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
					break;
				case "5":
				case "6":
				case "7":
				case "8":
					// logout (if customer-only or if case "8") / Approve or reject an account
					if (command[0].equals("8") || (command[0].equals("5") && !customer.isEmployee())) {
						CustomerServicesImpl.getInstance().logout();
						currentMenu = Menus.MAIN;
						return;
					} else handleEmployeeOptions(command);
				default: printMessage("Please enter a valid option."); return;
			}
//		}
	}
	
	private static void handleEmployeeOptions(String[] command) {
		/*
		 * "5. Approve or reject an account"
		 * "6. View a customer's accounts"
		 * "7. View transaction log"
		 */
		
		switch (command[0]) {
			case "5":
				// Approve or reject an account
				List<Account> pending = AccountServicesImpl.getInstance().getPendingAccounts();
				printMessage("Pending accounts:");
				for (Account a : pending) {
					Customer c = CustomerServicesImpl.getInstance().getCustomer(a.getCustomerId());
					printMessage("    Customer: %s, Requested balance: $%.2f%n", c.getUsername(), a.getBalance());
					boolean approved = getConfirmation("    Approved? ");
					if (approved) {
						a.setPending(false);
						AccountServicesImpl.getInstance().update(a);
					}
				}
				break;
			case "6":
				// View a customer's accounts
				// "6 {customer_id}
				if (command.length != 2) {
					printMessage("Please enter \"6\" followed by the customer id.");
					break;
				}
				Customer c = CustomerServicesImpl.getInstance().getCustomer(Integer.parseInt(command[1]));
				printAccounts(c);
				break;
			case "7":
				// View transaction log
				Map<Integer, Transaction> transactions = TransactionServicesImpl.getInstance().getAll();
				printMessage("\n\n\nTransaction Log:");
				transactions.values().stream().forEach((t) -> printMessage("	id: %d, source: %s, type: %s, amount: $%.2f, receiver: %s%n", t.getId(), t.getSource(), t.getType(), t.getAmount(), t.getReceiver()));
				printMessage("\n\n");
				break;
			default: break;
		}
	}
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		boolean quit = false;
		
		printMessage("Welcome to Raptor Inc.'s Automated Banking Interface!\n");
		do {
			switch (currentMenu) {
				case MAIN:
					printMenu(main_menu, "Quit", true);
					quit = handleMainMenu();
					break;
				case CUSTOMER_MAIN:
					printAccounts();
					printMenu(customer_menu, "Logout", true, CustomerServicesImpl.getInstance().getCustomer().isEmployee());
					handleCustomerMenu();
					break;
			}
		} while (!quit);
		
		scanner.close();
	}
}
