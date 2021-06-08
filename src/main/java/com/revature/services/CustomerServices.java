package com.revature.services;

import java.util.Map;
import java.util.Scanner;

import com.revature.models.Customer;

public interface CustomerServices {
	boolean login(Scanner scanner);
	boolean signUp(Scanner scanner);
	boolean logout();
	Map<Integer, Customer> getAllCustomers();
}
