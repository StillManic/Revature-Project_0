package com.revature.services;

import java.util.List;
import java.util.Scanner;

import com.revature.app.Driver;
import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Transaction;
import com.revature.repositories.AccountRepository;

public class AccountServicesImpl implements AccountServices {
	private static AccountServicesImpl instance;
	
	private AccountServicesImpl() {}
	
	public static AccountServicesImpl getInstance() {
		if (instance == null) instance = new AccountServicesImpl();
		return instance;
	}
	
	public List<Account> getPendingAccounts() {
		return AccountRepository.getInstance().getPendingAccounts();
	}
	
	private static String[] parseWithdrawOrDepositInfo(Scanner scanner, boolean withdraw) {
		String[] info = new String[2];
		Driver.printAccounts(CustomerServicesImpl.getInstance().getCustomer());
		Driver.printMessage("Please enter the account you wish to %s: ", withdraw ? "withdraw from" : "deposit into");
		info[0] = scanner.next();
		scanner.nextLine();
		Driver.printMessage("Amount to " + (withdraw ? "withdraw: " : "deposit: "), false);
		info[1] = scanner.next();
		scanner.nextLine();
		return info;
	}
	
	private static String[] parseTransferInfo(Scanner scanner) {
		String[] info = new String[3];
		Driver.printAccounts(CustomerServicesImpl.getInstance().getCustomer());
		Driver.printMessage("Please enter the account you wish to transfer from: ", false);
		info[0] = scanner.next();
		scanner.nextLine();
		Driver.printMessage("Please enter the account you wish to transfer to: ", false);
		info[1] = scanner.next();
		scanner.nextLine();
		Driver.printMessage("Amount to transfer: ", false);
		info[2] = scanner.next();
		scanner.nextLine();
		return info;
	}
	
	@Override
	public boolean withdraw(Scanner scanner) {
		String[] command = parseWithdrawOrDepositInfo(scanner, true);
		Integer account_id = Integer.parseInt(command[0]);
		Float amount = Float.parseFloat(command[1]);
		
		if (!CustomerServicesImpl.getInstance().getCustomer().getAccounts().containsKey(account_id)) {
			Driver.printMessage("You are not authorized to withdraw from account %d.%n", account_id);
			return false;
		}
		
		Account account = AccountRepository.getInstance().getById(account_id);
		if (account != null) {
			if (amount < 0) {
				Driver.printMessage("You cannot withdraw a negative amount.");
				return false;
			} else if (amount > account.getBalance()) {
				Driver.printMessage("You cannot withdraw $%.2f from account %d because its balance is only $%.2f.%n", amount, account_id, account.getBalance());
				return false;
			} else if (account.isPending()) {
				Driver.printMessage("You cannot withdraw money from a pending account. Please wait until an employee approves the account.");
				return false;
			} else {
				boolean confirmation = Driver.getConfirmation("Are you sure you wish to withdraw $%.2f from account %d?", amount, account_id);
				if (confirmation) {
					account.setBalance(account.getBalance() - amount);
					update(account, true);
					TransactionServicesImpl.getInstance().add(new Transaction(account, "withdrawal", amount));
					Driver.printMessage("You have withdrawn $%.2f from account %d.%n", amount, account_id);
					Driver.logger.info(String.format("Customer %s withdrew $%.2f from account %d.", CustomerServicesImpl.getInstance().getCustomer().getUsername(), amount, account_id));
					return true;
				}
			}
		} else {
			Driver.printMessage("You do not have an account with the id of " + account_id);
		}
		
		return false;
	}
	
	@Override
	public boolean deposit(Scanner scanner) {
		String[] info = parseWithdrawOrDepositInfo(scanner, false);
		Integer account_id = Integer.parseInt(info[0]);
		Float amount = Float.parseFloat(info[1]);
		
		if (!CustomerServicesImpl.getInstance().getCustomer().getAccounts().containsKey(account_id)) {
			Driver.printMessage("You are not authorized to deposit money into account %d.%n", account_id);
			return false;
		}
		
		Account account = AccountRepository.getInstance().getById(account_id);
		if (account != null) {
			if (amount < 0) {
				Driver.printMessage("You cannot deposit a negative amount.");
				return false;
			} else if (account.isPending()) {
				Driver.printMessage("You cannot deposit money into a pending account. Please wait until an employee approves the account.");
				return false;
			} else {
				account.setBalance(account.getBalance() + amount);
				update(account, true);
				TransactionServicesImpl.getInstance().add(new Transaction(account, "deposit", amount));
				Driver.printMessage("You have depositted $%.2f into account %d.%n", amount, account_id);
				Driver.logger.info(String.format("Customer %s depositted $%.2f into account %d.", CustomerServicesImpl.getInstance().getCustomer().getUsername(), amount, account_id));
				return true;
			}
		} else {
			Driver.printMessage("You do not have an account with the id of " + account_id);
			
		}
		return false;
	}
	
	@Override
	public boolean transfer(Scanner scanner) {
		String[] info = parseTransferInfo(scanner);
		Integer from_id = Integer.parseInt(info[0]);
		Integer to_id = Integer.parseInt(info[1]);
		Float amount = Float.parseFloat(info[2]);
		
		if (!CustomerServicesImpl.getInstance().getCustomer().getAccounts().containsKey(from_id)) {
			Driver.printMessage("You are not authorized to transfer money out of account %d.%n", from_id);
			return false;
		} else if (!CustomerServicesImpl.getInstance().getCustomer().getAccounts().containsKey(to_id)) {
			Driver.printMessage("You are not authorized to transfer money into account %d.%n", to_id);
			return false;
		}
		
		Account from = CustomerServicesImpl.getInstance().getCustomer().getAccounts().get(from_id);
		Account to = CustomerServicesImpl.getInstance().getCustomer().getAccounts().get(to_id);
		if (from == null) {
			Driver.printMessage("You do not have an account with the id of " + from_id);
			return false;
		} else if (to == null) {
			Driver.printMessage("You do not have an account with the id of " + to_id);
			return false;
		} else {
			if (amount < 0) {
				Driver.printMessage("You cannot transfer a negative amount.");
				return false;
			} else if (amount > from.getBalance()) {
				Driver.printMessage("You cannot transfer $%.2f from account %d because its balance is only $%.2f.", amount, from.getId(), from.getBalance());
				return false;
			} else if (from.isPending() || to.isPending()) {
				Driver.printMessage("One of the accounts you are attempting to transfer with has not been approved. The transfer could not be completed. Please wait until an employee approves the account(s).");
				return false;
			} else {
				boolean confirmation = Driver.getConfirmation("Are you sure you want to transfer $%.2f from account %d to account %d?", amount, from.getId(), to.getId());
				
				if (confirmation) {
					from.setBalance(from.getBalance() - amount);
					to.setBalance(to.getBalance() + amount);
					update(from, true);
					update(to, true);
					TransactionServicesImpl.getInstance().add(new Transaction(from, "transfer", amount, to));
					Driver.printMessage("Transfer confirmed.");
					Driver.logger.info(String.format("Customer %s transferred $%.2f from account %d to account %d.", CustomerServicesImpl.getInstance().getCustomer().getUsername(), amount, from_id, to_id));
					return true;
				} else {
					Driver.printMessage("Transfer canceled.");
					return false;
				}
			}
		}
	}
	
	@Override
	public void apply(Scanner scanner) {
		Driver.printMessage("How much money do you want in the account?: ", false);
		Float amount = scanner.nextFloat();
		scanner.nextLine();
		if (amount < 0) {
			Driver.printMessage("You cannot create an account with a negative balance.");
			return;
		}
		
		Account a = new Account(amount);
		a.setCustomerId(CustomerServicesImpl.getInstance().getCustomer().getId());
		if (!CustomerServicesImpl.getInstance().getCustomer().isEmployee())	a.setPending(true);
		AccountRepository.getInstance().add(a);
		CustomerServicesImpl.getInstance().getCustomer().addAccount(a);
		Driver.printMessage("Your new account with balance $%.2f has been created and is pending approval. It must be approved before it can be used.%n", amount);
	}
	
	@Override
	public void update(Account account, boolean updateCurrentCustomer) {
		if (updateCurrentCustomer) CustomerServicesImpl.getInstance().getCustomer().addAccount(account);
		AccountRepository.getInstance().update(account);
	}	
}
