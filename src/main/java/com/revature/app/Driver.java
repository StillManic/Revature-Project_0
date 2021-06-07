package com.revature.app;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.CustomerRepository;
import com.revature.services.AccountServicesImpl;
import com.revature.services.CustomerServicesImpl;

public class Driver {
	private enum Menus {
		MAIN,
		CUSTOMER_MAIN
	}
	
	private static Menus currentMenu = Menus.MAIN;
	
	public static final Logger logger = LogManager.getLogger(Driver.class);
	
	private static Scanner scanner;
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
				printMessage("    %d: $%.2f%n%n", e.getKey(), e.getValue().getBalance());
			});
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
		for (int i = 0; i < employee_options.length; i++) printMessage("%d. %s%n", i + menu.length, employee_options[i]);
		if (extraOption != null && !extraOption.isEmpty()) printMessage("%d. %s%n", menu.length + employee_options.length, extraOption);
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
	
//	private static String[] parseLoginInfo(boolean signingUp) {
//		String[] info = new String[2];
//		System.out.printf("Please enter %s login information:%n", signingUp ? "new" : "your");
//		System.out.print("Username: ");
//		info[0] = scanner.next();
//		System.out.print("Password: ");
//		info[1] = scanner.next();
//		return info;
//	}
	
	/**
	 * Read and process user input for the main menu.
	 * It is assumed that the menu has already been printed with printMenu().
	 * 
	 * @return whether the main do-while loop should terminate ("quit") on the next iteration
	 */
	private static boolean handleMainMenu() {
		while (true) {
			switch (scanner.nextInt()) {
				case 1:		// login
//					String[] loginInfo = parseLoginInfo(false);
//					customer = CustomerRepository.getInstance().getByUsernameAndPassword(loginInfo[0], loginInfo[1]);
//					if (customer == null) {
//						System.out.println("No customer account was found with that login information.\n");
//						printMenu(main_menu, "Quit", true);
//					} else {
//						System.out.println("Logged in with account: " + customer);
//						currentMenu = Menus.CUSTOMER_MAIN;
//						return false;
//					}
					
					if (CustomerServicesImpl.getInstance().login(scanner)) {
						currentMenu = Menus.CUSTOMER_MAIN;
						logger.info("Customer %s logged in.", CustomerServicesImpl.getInstance().getCustomer().getUsername());
						return false;
					} else printMenu(main_menu, "Quit", true);
					break;
				case 2:		// sign up
//					String[] signUpInfo = parseLoginInfo(true);
//					customer = CustomerRepository.getInstance().getByUsernameAndPassword(signUpInfo[0], signUpInfo[1]);
//					if (customer != null) {
//						System.out.println("An account with that login information already exists, please try again.\n");
//						printMenu(main_menu, "Quit", true);
//					} else {
//						customer = new Customer(signUpInfo[0], signUpInfo[1]);
//						CustomerRepository.getInstance().add(customer);
//						System.out.println("Logged in with account: " + customer);
//						currentMenu = Menus.CUSTOMER_MAIN;
//						return false;
//					}
					
					if (CustomerServicesImpl.getInstance().signUp(scanner)) {
						currentMenu = Menus.CUSTOMER_MAIN;
						logger.info("Customer %s has signed up.", CustomerServicesImpl.getInstance().getCustomer().getUsername());
						return false;
					} else printMenu(main_menu, "Quit", true);
					break;
				case 3: printMessage("Goodbye!"); return true;
				default: printMessage("Please enter a valid option."); break;
			}
		}
	}
	
	private static void handleCustomerMenu() {
		/*
		 * "1. Apply for a new account"
		 * "2. Withdraw money"
		 * "3. Deposit money"
		 * "4. Transfer money"
		 * "5. Logout
		 */
		String[] command;
		int fromID, toID;
//		Account fromAccount, toAccount;
		float amount;
		boolean success;
		Customer customer = CustomerServicesImpl.getInstance().getCustomer();
		if (customer == null) {
			logger.error("handleCustomerMenu: customer null");
			return;
		}
		
		while (true) {
			command = scanner.nextLine().split(" ");
			switch (command[0]) {
				case "1":
					// apply for a new account
					// TODO: implement this!!!
					printMessage("You cannot apply for a new account at this time.");
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
						logger.info("Customer %s withdrew $%.2f from account %d.", customer.getUsername(), amount, fromID);
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
//					fromAccount = customer.getAccounts().get(fromID);
//					amount = Float.parseFloat(command[2]);
//					if (amount < 0) {
//						System.out.println("You cannot withdraw a negative amount.");
//						break;
//					} else if (amount > fromAccount.getBalance()) {
//						System.out.printf("You cannot withdraw $%.2f from account %d because its balance is only $%.2f.%n", amount, fromID, fromAccount.getBalance());
//						break;
//					} else {
//						System.out.printf("Are you sure you wish to withdraw $%.2f from account %d? Y/N: ", amount, fromID);
//						boolean confirmation = scanner.nextLine().equalsIgnoreCase("y");
//						if (confirmation) {
//							fromAccount.setBalance(fromAccount.getBalance() - amount);
//							System.out.printf("You have withdrawn $%.2f from account %d.%n%n", amount, fromID);
//							printAccounts();
//							printMenu(customer_menu, "Logout", true);
//						}
//					}
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
						logger.info("Customer %s depositted $%.2f into account %d.", customer.getUsername(), amount, fromID);
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
//					fromAccount = customer.getAccounts().get(fromID);
//					amount = Float.parseFloat(command[2]);
//					if (amount < 0) {
//						System.out.println("You cannot deposit a negative amount.");
//						break;
//					} else {
//						fromAccount.setBalance(fromAccount.getBalance() + amount);
//						System.out.printf("You have depositted $%.2f into account %d.%n%n", amount, fromID);
//						printAccounts();
//						printMenu(customer_menu, "Logout", true);
//					}
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
						logger.info("Customer %s transferred $%.2f from account %d to account %d.", customer.getUsername(), amount, fromID, toID);
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					
//					boolean
//					fromAccount = customer.getAccounts().get(fromID);
//					toAccount = customer.getAccounts().get(toID);
//					amount = Float.parseFloat(command[3]);
//					if (amount < 0) {
//						System.out.println("You cannot transfer a negative amount.");
//						break;
//					} else {
//						
//					}
					
					break;
				case "5":
					// logout
					CustomerServicesImpl.getInstance().logout();
					currentMenu = Menus.MAIN;
					return;
				default: printMessage("Please enter a valid option."); return;
			}
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
