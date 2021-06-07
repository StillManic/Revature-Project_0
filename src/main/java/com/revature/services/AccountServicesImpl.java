package com.revature.services;

import com.revature.app.Driver;
import com.revature.models.Account;
import com.revature.models.Transaction;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.TransactionRepository;

public class AccountServicesImpl implements AccountServices {
	private static AccountServicesImpl instance;
	
	private AccountServicesImpl() {}
	
	public static AccountServicesImpl getInstance() {
		if (instance == null) instance = new AccountServicesImpl();
		return instance;
	}

	@Override
	public boolean withdraw(Integer account_id, Float amount) {
		Account account = AccountRepository.getInstance().getById(account_id);
		if (account != null) {
			if (amount < 0) {
				Driver.printMessage("You cannot withdraw a negative amount.");
				return false;
			} else if (amount > account.getBalance()) {
				Driver.printMessage("You cannot withdraw $%.2f from account %d because its balance is only $%.2f.%n", amount, account_id, account.getBalance());
				return false;
			} else {
				boolean confirmation = Driver.getConfirmation("Are you sure you wish to withdraw $%.2f from account %d?", amount, account_id);
				if (confirmation) {
					account.setBalance(account.getBalance() - amount);
					TransactionRepository.getInstance().add(new Transaction(account, "withdrawal", amount));
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean deposit(Integer account_id, Float amount) {
		Account account = AccountRepository.getInstance().getById(account_id);
		if (account != null) {
			if (amount < 0) {
				Driver.printMessage("You cannot deposit a negative amount.");
				return false;
			} else {
				account.setBalance(account.getBalance() + amount);
				TransactionRepository.getInstance().add(new Transaction(account, "deposit", amount));
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean transfer(Integer from_id, Integer to_id, Float amount) {
		Account from = AccountRepository.getInstance().getById(from_id);
		Account to = AccountRepository.getInstance().getById(to_id);
		if (from != null && to != null) {
			if (amount < 0) {
				Driver.printMessage("You cannot transfer a negative amount.");
				return false;
			} else if (amount > from.getBalance()) {
				Driver.printMessage("You cannot transfer $%.2f from account %d because its balance is only $%.2f.", amount, from.getId(), from.getBalance());
				return false;
			} else {
				boolean confirmation = Driver.getConfirmation("Are you sure you want to transfer $%.2f from account %d to account %d? Y/N: ", amount, from.getId(), to.getId());
				
				if (confirmation) {
					from.setBalance(from.getBalance() - amount);
					to.setBalance(to.getBalance() + amount);
					TransactionRepository.getInstance().add(new Transaction(from, "transfer", amount, to));
					return true;
				} else {
					Driver.printMessage("Transfer canceled.");
					return false;
				}
			}
		} else {
			Driver.printMessage("One of the provided accounts does not exist.");
			return false;
		}
	}
	
	@Override
	public void apply(Float amount) {
		
	}
}
