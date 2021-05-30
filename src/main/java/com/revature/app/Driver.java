package com.revature.app;

import java.util.Scanner;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.CustomerRepository;
import com.revature.utils.MockDB;

public class Driver {
	private static Scanner scanner;
	private static String[] main_menu = { "1. Login", "2. Sign Up" };
	
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
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		boolean quit = false;
		
		System.out.println("Welcome to Raptor Inc.'s Automated Banking Interface!\n");
		printMenu(main_menu, "Quit", true);
		Customer customer;
		do {
			switch (scanner.nextInt()) {
				case 1:
					String[] loginInfo = parseLoginInfo(false);
					customer = CustomerRepository.getInstance().getByUsernameAndPassword(loginInfo[0], loginInfo[1]);
					if (customer == null) {
						System.out.println("No customer account was found with that login information.\n");
						printMenu(main_menu, "Quit", true);					
					} else {
						System.out.println("Logged in with account: " + customer);
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
					}
					break;
				case 3:
					System.out.println("Goodbye!");
					quit = true;
					break;
				default: 
					System.out.println("Please enter a valid option.");
					printCarrot();
					break;
			}
		} while (!quit);
		
		
		
		scanner.close();
	}
}
