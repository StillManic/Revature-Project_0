package com.revature.utils;

import java.util.HashMap;
import java.util.Map;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.CustomerRepository;

public class MockDB {
	public static Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
	public static Map<Integer, Customer> customers = new HashMap<Integer, Customer>();
	public static Map<Integer, Account> accounts = new HashMap<Integer, Account>();
	
	static {
		employees.put(1, new Employee(1, "gerald", "strong_password"));
		
		customers.put(1, new Customer(1, "jessica", "camelot"));
		customers.put(2, new Customer(2, "batman", "robin"));
		customers.put(3, new Customer(3, "lesly", "lesly"));
		customers.put(4, new Customer(4, "jason", "jiu_jitsu"));
		
		accounts.put(1, new Account(1, 250000.00f));
		accounts.put(2, new Account(2, 8000000.00f));
		accounts.put(3, new Account(3, 100000.00f));
		accounts.put(4, new Account(4, 1000.00f));
		
		for (int i = 1; i < 5; i++) customers.get(i).addAccount(accounts.get(i));
	}
}
