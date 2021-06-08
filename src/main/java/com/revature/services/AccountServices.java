package com.revature.services;

import java.util.Scanner;

import com.revature.models.Account;

public interface AccountServices {
	boolean withdraw(Scanner scanner);
	boolean deposit(Scanner scanner);
	boolean transfer(Scanner scanner);
	void apply(Scanner scanner);
	void update(Account account, boolean updateCurrentCustomer);
}
