package com.revature.app;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.CustomerRepository;
import com.revature.utils.MockDB;

public class Driver {
	private enum Menus {
		MAIN,
		CUSTOMER_MAIN
	}
	
	private static Menus currentMenu = Menus.MAIN;
	
	public static final Logger logger = LogManager.getLogger(Driver.class);
	
	private static Scanner scanner;
	private static String[] main_menu = { "1. Login", "2. Sign Up" };
	private static String[] customer_menu = { "1. Apply for a new account", "2. Withdraw money", "3. Deposit money", "4. Post a money transfer", "5. Accept a money transfer"};
	
	private static Customer customer;
	
	static {
//		MockDB.employees.entrySet().stream().forEach((e) -> System.out.println(e));
//		System.out.println();
//		MockDB.customers.entrySet().stream().forEach((c) -> System.out.println(c));
//		System.out.println();
//		MockDB.accounts.entrySet().stream().forEach((a) -> System.out.println(a));
//		
//		AccountRepository accRepo = AccountRepository.getInstance();
//		CustomerRepository cusRepo = CustomerRepository.getInstance();
//		Customer newBatman = new Customer("batman", "not_robin", accRepo.getById(1));
//		newBatman.setId(1);
////		Customer newBatman = new Customer(1, "batman", "not_robin");
////		newBatman.addAccount(accRepo.getById(1));
//		cusRepo.update(newBatman);
//		
//		Account newAccount = new Account(4, 2000);
//		accRepo.update(newAccount);
//		
//		System.out.println("\nCustomers after update():");
//		MockDB.customers.entrySet().stream().forEach((c) -> System.out.println(c));
//		System.out.println("\nAccounts after update():");
//		MockDB.accounts.entrySet().stream().forEach((a) -> System.out.println(a));
	}
	
	private static void printAccounts() {
		System.out.println("\nYou are logged in as user \"" + customer.getUsername() + "\"\n");
		if (customer.getAccounts() == null || customer.getAccounts().size() == 0) {
			System.out.println("You do not have any accounts currently open.");
		} else {
			System.out.println("Your accounts:");
			customer.getAccounts().entrySet().forEach((e) -> {
				System.out.printf("    %d: $%.2f%n%n", e.getKey(), e.getValue().getBalance());
			});
		}
	}
	
	private static void printMenu(String[] menu, String extraOption, boolean printCarrot) {
		System.out.println("Please select an option:\n");
		for (String option : menu) System.out.println(option);
		if (extraOption != null && !extraOption.isEmpty()) System.out.println("" + (menu.length + 1) + ". " + extraOption);
		if (printCarrot) printCarrot();
	}
	
	private static void printCarrot() {
		System.out.print("> ");
	}
	
	private static String[] parseLoginInfo(boolean signingUp) {
		String[] info = new String[2];
		System.out.printf("Please enter %s login information:%n", signingUp ? "new" : "your");
		System.out.print("Username: ");
		info[0] = scanner.next();
		System.out.print("Password: ");
		info[1] = scanner.next();
		return info;
	}
	
	/**
	 * Read and process user input for the main menu.
	 * It is assumed that the menu has already been printed with printMenu().
	 * 
	 * @return whether the main do-while loop should terminate ("quit") on the next iteration
	 */
	private static boolean handleMainMenu() {
		while (true) {
			switch (scanner.nextInt()) {
				case 1:
					String[] loginInfo = parseLoginInfo(false);
					customer = CustomerRepository.getInstance().getByUsernameAndPassword(loginInfo[0], loginInfo[1]);
					if (customer == null) {
						System.out.println("No customer account was found with that login information.\n");
						printMenu(main_menu, "Quit", true);
					} else {
						System.out.println("Logged in with account: " + customer);
						currentMenu = Menus.CUSTOMER_MAIN;
						return false;
					}
					break;
				case 2:
					String[] signUpInfo = parseLoginInfo(true);
					customer = CustomerRepository.getInstance().getByUsernameAndPassword(signUpInfo[0], signUpInfo[1]);
					if (customer != null) {
						System.out.println("An account with that login information already exists, please try again.\n");
						printMenu(main_menu, "Quit", true);
					} else {
						customer = new Customer(signUpInfo[0], signUpInfo[1]);
						CustomerRepository.getInstance().add(customer);
						System.out.println("Logged in with account: " + customer);
						currentMenu = Menus.CUSTOMER_MAIN;
						return false;
					}
					break;
				case 3: System.out.println("Goodbye!"); return true;
				default: System.out.println("Please enter a valid option."); break;
			}
		}
	}
	
	private static void handleCustomerMenu() {
		/*
		 * "1. Apply for a new account"
		 * "2. Withdraw money"
		 * "3. Deposit money"
		 * "4. Post a money transfer"
		 * "5. Accept a money transfer"
		 * "6. Logout"
		 */
		String[] command;
		int accountID;
		Account account;
		float amount;
		while (true) {
			command = scanner.nextLine().split(" ");
			switch (command[0]) {
				case "1":
					// apply for a new account
					System.out.println("You cannot apply for a new account at this time.");
					break;
				case "2":
					// withdraw money - "2 {account_id} {amount}"
					if (command.length != 3) {
						System.out.println("Please enter \"2\" followed by the account number followed by the amount you wish to withdraw.");
						break;
					}
					
					accountID = Integer.parseInt(command[1]);
					if (!customer.getAccounts().containsKey(accountID)) {
						System.out.println("You do not have an account with the id of " + accountID);
						break;
					}
					
					account = customer.getAccounts().get(accountID);
					amount = Float.parseFloat(command[2]);
					if (amount < 0) {
						System.out.println("You cannot withdraw a negative amount.");
						break;
					} else if (amount > account.getBalance()) {
						System.out.printf("You cannot withdraw $%.2f from account %d because its balance is only $%.2f.%n", amount, accountID, account.getBalance());
						break;
					} else {
						System.out.printf("Are you sure you wish to withdraw $%.2f from account %d? Y/N: ", amount, accountID);
						boolean confirmation = scanner.nextLine().equalsIgnoreCase("y");
						if (confirmation) {
							account.setBalance(account.getBalance() - amount);
							System.out.printf("You have withdrawn $%.2f from account %d.%n%n", amount, accountID);
							printAccounts();
							printMenu(customer_menu, "Logout", true);
						}
					}
					break;
				case "3":
					// deposit money - "3 {account_id} {amount}"
					if (command.length != 3) {
						System.out.println("Please enter \"3\" followed by the account number followed by the amount you wish to withdraw.");
						break;
					}
					
					accountID = Integer.parseInt(command[1]);
					if (!customer.getAccounts().containsKey(accountID)) {
						System.out.println("You do not have an account with the id of " + accountID);
						break;
					}
					
					account = customer.getAccounts().get(accountID);
					amount = Float.parseFloat(command[2]);
					if (amount < 0) {
						System.out.println("You cannot deposit a negative amount.");
						break;
					} else {
						account.setBalance(account.getBalance() + amount);
						System.out.printf("You have depositted $%.2f into account %d.%n%n", amount, accountID);
						printAccounts();
						printMenu(customer_menu, "Logout", true);
					}
					break;
				case "4":
					// post money transfer
				case "5":
					// accept money transfer
				case "6":
					// logout
					customer = null;
					System.out.println("You have been logged out.\n");
					currentMenu = Menus.MAIN;
					return;
				default: System.out.println("Please enter a valid option."); return;
			}
		}
	}
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		boolean quit = false;
		
		System.out.println("Welcome to Raptor Inc.'s Automated Banking Interface!\n");
//		printMenu(main_menu, "Quit", true);
//		Customer customer;
		do {
			switch (currentMenu) {
				case MAIN:
					printMenu(main_menu, "Quit", true);
					quit = handleMainMenu();
					break;
				case CUSTOMER_MAIN:
					printAccounts();
					printMenu(customer_menu, "Logout", true);
					handleCustomerMenu();
					break;
			}
		} while (!quit);
		
		
		
		scanner.close();
	}
}
